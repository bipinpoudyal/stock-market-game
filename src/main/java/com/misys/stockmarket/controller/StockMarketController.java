package com.misys.stockmarket.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.misys.stockmarket.constants.IApplicationConstants;
import com.misys.stockmarket.domain.entity.LeagueMaster;
import com.misys.stockmarket.domain.entity.StockCurrentQuotes;
import com.misys.stockmarket.domain.entity.UserAlerts;
import com.misys.stockmarket.enums.STOCK_ERR_CODES;
import com.misys.stockmarket.exception.LeagueException;
import com.misys.stockmarket.exception.ServiceException;
import com.misys.stockmarket.exception.service.AlertsServiceException;
import com.misys.stockmarket.exception.service.OrderServiceException;
import com.misys.stockmarket.exception.service.StockServiceException;
import com.misys.stockmarket.mbeans.OrderFormBean;
import com.misys.stockmarket.mbeans.StockCurrentQuotesBean;
import com.misys.stockmarket.mbeans.StockHistoryBean;
import com.misys.stockmarket.mbeans.StockMasterBean;
import com.misys.stockmarket.mbeans.UserAlertsBean;
import com.misys.stockmarket.mbeans.UserNotificationsFormBean;
import com.misys.stockmarket.mbeans.WatchListFormBean;
import com.misys.stockmarket.platform.web.ResponseMessage;
import com.misys.stockmarket.services.AlertsService;
import com.misys.stockmarket.services.LeagueService;
import com.misys.stockmarket.services.OrderService;
import com.misys.stockmarket.services.StockService;

/**
 * @author Gurudath Reddy
 * @version 1.0
 */
@Controller
public class StockMarketController {

	private static final Log LOG = LogFactory
			.getLog(StockMarketController.class);

	@Inject
	private StockService stockService;

	@Inject
	private OrderService orderService;
	
	@Inject
	private LeagueService leagueService;

	@Inject
	private AlertsService alertsService;

	@RequestMapping(value = "/buystock", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseMessage buyStock(@RequestBody OrderFormBean orderFormBean) {
		orderFormBean.setType(IApplicationConstants.BUY_TYPE);
		try {
			LeagueMaster league = leagueService.getLeagueById(orderFormBean.getLeagueUserId());
			if(league.getStage().compareTo(new BigDecimal(2)) == 0)
			{
				if(IApplicationConstants.ORDER_PRICE_TYPE_STOPLOSS.equals(orderFormBean
						.getPriceType()))
				{
					return new ResponseMessage(ResponseMessage.Type.error,
							"Stop Loss orders are not allowed for this league");
				}
			}
			if(league.getStage().compareTo(new BigDecimal(3)) == 0)
			{
				if(IApplicationConstants.ORDER_PRICE_TYPE_STOPLOSS.equals(orderFormBean
						.getPriceType()) || IApplicationConstants.ORDER_PRICE_TYPE_LIMIT.equals(orderFormBean
								.getPriceType()))
				{
					return new ResponseMessage(ResponseMessage.Type.error,
							"Stop Loss and Limit orders are not allowed for this league");
				}
			}
			orderService.saveNewOrder(orderFormBean);
			return new ResponseMessage(ResponseMessage.Type.success,
					"Your order has been placed.");
		} catch (OrderServiceException e) {
			return new ResponseMessage(ResponseMessage.Type.danger,
					"There was a technical error while processing your order. Please try again");
		} catch (LeagueException e) {
			return new ResponseMessage(ResponseMessage.Type.danger,
				"There was a technical error while processing your order. Please try again");
		}
	}

	@RequestMapping(value = "/sellstock", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseMessage sellStock(@RequestBody OrderFormBean orderFormBean) {
		orderFormBean.setType(IApplicationConstants.SELL_TYPE);
		try {
			LeagueMaster league = leagueService.getLeagueById(orderFormBean.getLeagueUserId());
			if(league.getStage().compareTo(new BigDecimal(2)) == 0)
			{
				if(IApplicationConstants.ORDER_PRICE_TYPE_STOPLOSS.equals(orderFormBean
						.getPriceType()))
				{
					return new ResponseMessage(ResponseMessage.Type.error,
							"Stop Loss orders are not allowed for this league");
				}
			}
			if(league.getStage().compareTo(new BigDecimal(3)) == 0)
			{
				if(IApplicationConstants.ORDER_PRICE_TYPE_STOPLOSS.equals(orderFormBean
						.getPriceType()) || IApplicationConstants.ORDER_PRICE_TYPE_LIMIT.equals(orderFormBean
								.getPriceType()))
				{
					return new ResponseMessage(ResponseMessage.Type.error,
							"Stop Loss and Limit orders are not allowed for this league");
				}
			}
			orderService.saveNewOrder(orderFormBean);
			return new ResponseMessage(ResponseMessage.Type.success,
					"Your order has been placed.");
		} catch (OrderServiceException e) {
			return new ResponseMessage(ResponseMessage.Type.danger,
					"There was a technical error while processing your order. Please try again");
		} catch (LeagueException e) {
			return new ResponseMessage(ResponseMessage.Type.danger,
				"There was a technical error while processing your order. Please try again");
		}
	}

	@RequestMapping(value = "/watchStock", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseMessage watchStock(
			@RequestBody WatchListFormBean watchListFormBean) {
		return alertsService.saveNewWatchStock(watchListFormBean);
	}

	@RequestMapping(value = "/alertList", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public List<UserAlertsBean> alertList() {
		try {
			List<UserAlerts> alerts = alertsService.listAllAlerts();
			List<UserAlertsBean> userAlertsBeans = new ArrayList<UserAlertsBean>();
			for(Iterator<UserAlerts> iterator = alerts
					.iterator(); iterator.hasNext();)
			{
				UserAlerts userAlerts = (UserAlerts) iterator.next();
				UserAlertsBean userAlertsBean = new UserAlertsBean();
				userAlertsBean.setMessage(userAlerts.getMessage());
				userAlertsBean.setNotifiedDate(userAlerts.getNotifiedDate().toString());
				userAlertsBeans.add(userAlertsBean);
			}
			Collections.reverse(userAlertsBeans);
			return userAlertsBeans;
		} catch (AlertsServiceException e) {
			LOG.error(e);
			// TODO: HANDLE WHEN EXCEPTION
			return null;
		}
	}
	
	@RequestMapping(value = "/userNotifications", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public UserNotificationsFormBean userNotifications() {
		try {
				UserNotificationsFormBean bean = new UserNotificationsFormBean();
				List<UserAlerts> alerts = alertsService.listAllAlerts();
				List<UserAlertsBean> userAlertsBeans = new ArrayList<UserAlertsBean>();
				List<UserAlertsBean> userNotificationBeans = new ArrayList<UserAlertsBean>();
				for(Iterator<UserAlerts> iterator = alerts
						.iterator(); iterator.hasNext();)
				{
					UserAlerts userAlerts = (UserAlerts) iterator.next();
					UserAlertsBean userAlertsBean = new UserAlertsBean();
					userAlertsBean.setMessage(userAlerts.getMessage());
					userAlertsBean.setNotifiedDate(userAlerts.getNotifiedDate().toString());
					if(IApplicationConstants.ALERT_TYPE_WATCH_LIST.equalsIgnoreCase(userAlerts.getAlertType()))
					{
						userAlertsBeans.add(userAlertsBean);
					}
					else
					{
						userNotificationBeans.add(userAlertsBean);
					}
					
				}
				Collections.reverse(userAlertsBeans);
				Collections.reverse(userNotificationBeans);
				if(userAlertsBeans.size() >= 5)
				{
					userAlertsBeans = userAlertsBeans.subList(0, 5);
				}
				
				if(userNotificationBeans.size() >= 5)
				{
					userNotificationBeans = userNotificationBeans.subList(0, 5);
				}
				
				bean.setAlertsList(userAlertsBeans);
				bean.setNotificationsList(userNotificationBeans);
				return bean;
		} catch (AlertsServiceException e) {
			LOG.error(e);
			// TODO: HANDLE WHEN EXCEPTION
			return null;
		}
	}
	
	
	

	@RequestMapping(value = "/stockList", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public List<StockMasterBean> stockList() {
		try {
			return stockService.listAllActiveStockBeans();
		} catch (StockServiceException e) {
			LOG.error(e);
			// TODO: HANDLE WHEN EXCEPTION
			return null;
		}
	}

	@RequestMapping(value = "/stockListCurrentQuotes", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public StockCurrentQuotesBean stockListCurrentQuotes(
			@RequestParam("stockSymbol") String symbol) {
		try {
			return stockService.getStockCurrentQuoteByStockSymbol(symbol);
		} catch (StockServiceException e) {
			if (STOCK_ERR_CODES.CURRENT_DAY_STOCK_NOT_FOUND.compareTo(e
					.getErrorCode()) == 0) {
				LOG.error("Current Day Stock Not Found for Ticker Symbol: "
						+ symbol, e);
			} else {
				LOG.error(e);
			}
			// TODO: HANDLE WHEN EXCEPTION
			return null;
		}
	}

	@RequestMapping(value = "/stockListAllCurrentQuotes", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<StockCurrentQuotesBean> stockListAllCurrentQuotes() {
		try {
			return stockService.listAllCurrentStockQuotesBean();
		} catch (ServiceException e) {
			LOG.error(e);
			// TODO: HANDLE WHEN EXCEPTION
			return null;
		}
	}

	@RequestMapping(value = "/stockListHistory", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public List<StockHistoryBean> stockListHistory(
			@RequestParam("stockSymbol") String symbol) {
		try {
			return stockService.listStockHistory(symbol);
		} catch (StockServiceException e) {
			LOG.error(e);
			// TODO: HANDLE WHEN EXCEPTION
			return null;
		}
	}

	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}
}
