package com.misys.stockmarket.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.misys.stockmarket.achievements.AchievementFacade;
import com.misys.stockmarket.constants.IApplicationConstants;
import com.misys.stockmarket.dao.LeagueDAO;
import com.misys.stockmarket.dao.OrderExecutionDAO;
import com.misys.stockmarket.dao.OrderMasterDAO;
import com.misys.stockmarket.domain.entity.LeagueUser;
import com.misys.stockmarket.domain.entity.OrderExecution;
import com.misys.stockmarket.domain.entity.OrderMaster;
import com.misys.stockmarket.domain.entity.StockCurrentQuotes;
import com.misys.stockmarket.domain.entity.StockMaster;
import com.misys.stockmarket.exception.BaseException;
import com.misys.stockmarket.exception.DAOException;
import com.misys.stockmarket.exception.LeagueException;
import com.misys.stockmarket.mbeans.MyPortfolioFormBean;
import com.misys.stockmarket.mbeans.StockHoldingFormBean;

@Service("orderExecutionService")
@Repository
public class OrderExecutionService {

	private static final Log LOG = LogFactory
			.getLog(OrderExecutionService.class);

	@Inject
	private OrderMasterDAO orderDAO;

	@Inject
	private OrderExecutionDAO orderExecutionDAO;

	@Inject
	private LeagueDAO leagueDAO;

	@Inject
	private StockService stockService;

	@Inject
	private LeagueService leagueService;

	@Inject
	private PortfolioService portfolioService;

	@Inject
	private AchievementFacade achievementFacade;

	public void executeOrders() {
		List<OrderMaster> pendingOrderList;
		try {
			pendingOrderList = orderDAO.findAllPendingOrders();
			for (OrderMaster orderMaster : pendingOrderList) {
				try {
					executeOrder(orderMaster);
				} catch (BaseException e) {
					LOG.error(e);
				}
			}
		} catch (DAOException daoException) {
			LOG.error(daoException);
		}
	}

	/**
	 * @param orderMaster
	 * @throws BaseException
	 */
	@Transactional(rollbackFor = BaseException.class)
	private void executeOrder(OrderMaster orderMaster) throws BaseException {
		if (isEligible(orderMaster)) {
			LOG.info("Executing the order " + orderMaster.getOrderId());
			try {
				BigDecimal volume = orderMaster.getVolume();
				BigDecimal executionPrice = getCurrentPrice(
						orderMaster.getStockMaster()).multiply(volume);
				LeagueUser leagueUser = leagueService
						.getLeagueUserById(orderMaster.getLeagueUser()
								.getLeagueUserId());
				MyPortfolioFormBean portfolioBean = portfolioService
						.getMyPortfolio(leagueUser.getLeagueMaster()
								.getLeagueId(), leagueUser.getUserMaster()
								.getUserId());

				List<StockHoldingFormBean> stockHoldings = portfolioBean
						.getStockHoldings();

				Map<String, StockHoldingFormBean> stockHoldingBySymbolMap = new HashMap<String, StockHoldingFormBean>();

				for (StockHoldingFormBean stockHolding : stockHoldings) {
					stockHoldingBySymbolMap.put(stockHolding.getTikerSymbol(),
							stockHolding);
				}
				if (IApplicationConstants.BUY_TYPE
						.equals(orderMaster.getType())) {
					if (leagueUser.getRemainingAmount().compareTo(
							executionPrice) >= 0) {
						// SUBSTRACT FROM LEAGUE AMOUNT
						leagueUser.setRemainingAmount(leagueUser
								.getRemainingAmount().subtract(executionPrice));
						leagueDAO.update(leagueUser);
						// Create order execution entry
						OrderExecution orderExecution = new OrderExecution();
						orderExecution.setOrderMaster(orderMaster);
						orderExecution.setUnitsTraded(volume);
						orderExecution.setExecutionPrice(executionPrice);
						orderExecution.setExecutionDate(new Date());
						orderDAO.persist(orderExecution);
						orderMaster
								.setStatus(IApplicationConstants.ORDER_STATUS_COMPLETED);
					} else {
						orderMaster
								.setStatus(IApplicationConstants.ORDER_STATUS_INSUFFICIENT_FUNDS);
					}
				} else {
					StockHoldingFormBean stockBean = stockHoldingBySymbolMap
							.get(orderMaster.getStockMaster().getTikerSymbol());
					if (stockBean != null
							&& (new BigDecimal(stockBean.getVolume())
									.compareTo(orderMaster.getVolume()) >= 0)) {
						// ADD TO LEAGUE AMOUNT
						leagueUser.setRemainingAmount(leagueUser
								.getRemainingAmount().add(executionPrice));
						leagueDAO.update(leagueUser);
						// Create order execution entry
						OrderExecution orderExecution = new OrderExecution();
						orderExecution.setOrderMaster(orderMaster);
						orderExecution.setUnitsTraded(volume);
						orderExecution.setExecutionPrice(executionPrice);
						orderExecution.setExecutionDate(new Date());
						orderDAO.persist(orderExecution);
						orderMaster
								.setStatus(IApplicationConstants.ORDER_STATUS_COMPLETED);
					} else {
						orderMaster
								.setStatus(IApplicationConstants.ORDER_STATUS_INSUFFICIENT_VOLUME);
					}

				}
				LOG.info("Completed executing the order "
						+ orderMaster.getOrderId());
				// Update order master entry
				orderExecutionDAO.update(orderMaster);
				// Evaluate achievements
				List<String> categories = new ArrayList<String>();
				categories.add("buyOrder");
				categories.add("sellOrder");
				categories.add("safeOrders");
				categories.add("marketOrders");
				categories.add("differentCompanies");
				categories.add("singleDayOrders");
				achievementFacade.evaluate(leagueUser.getUserMaster(),
						categories);
			} catch (LeagueException e) {
				LOG.error(e);
			}

		}
	}

	private BigDecimal getCurrentPrice(StockMaster stockMaster) {
		StockCurrentQuotes stockCurrentQuotes = stockService
				.getStockCurrentQuoteByStockId(stockMaster.getStockId());
		return stockCurrentQuotes.getLastTradePriceOnly();
	}

	private boolean isEligible(OrderMaster orderMaster) {
		boolean isEligible = false;
		StockCurrentQuotes stockCurrentQuotes = stockService
				.getStockCurrentQuoteByStockId(orderMaster.getStockMaster()
						.getStockId());
		if (IApplicationConstants.ORDER_PRICE_TYPE_MARKET.equals(orderMaster
				.getPriceType())) {
			// TODO: Changed for demo need to revert back later
			/*if (stockCurrentQuotes.getUpdatedTimeStamp().after(
					orderMaster.getOrderDate())) {*/
				isEligible = true;
			//}
		} else if (IApplicationConstants.ORDER_PRICE_TYPE_LIMIT
				.equals(orderMaster.getPriceType())) {
			if (stockCurrentQuotes.getUpdatedTimeStamp().after(
					orderMaster.getOrderDate())) {
				// IN CASE OF BUY THE MARKET PRICE SHOULD BE LESS THAN OR EQUAL
				// TO LIMIT PRICE
				if (IApplicationConstants.BUY_TYPE
						.equals(orderMaster.getType())) {
					if (stockCurrentQuotes.getLastTradePriceOnly().compareTo(
							orderMaster.getOrderPrice()) <= 0) {
						isEligible = true;
					}
				}
				// IN CASE OF SELL THE MARKET PRICE SHOULD BE GREATER THAN OR
				// EQUAL TO LIMIT PRICE
				else if (IApplicationConstants.SELL_TYPE.equals(orderMaster
						.getType())) {
					if (stockCurrentQuotes.getLastTradePriceOnly().compareTo(
							orderMaster.getOrderPrice()) >= 0) {
						isEligible = true;
					}
				}
			}
		} else if (IApplicationConstants.ORDER_PRICE_TYPE_STOPLOSS
				.equals(orderMaster.getPriceType())) {
			if (IApplicationConstants.SELL_TYPE.equals(orderMaster.getType())) {
				if (stockCurrentQuotes.getLastTradePriceOnly().compareTo(
						orderMaster.getOrderPrice()) <= 0) {
					isEligible = true;
				}
			}
		}
		return isEligible;
	}
}
