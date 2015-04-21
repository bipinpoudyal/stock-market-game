package vsm;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.misys.stockmarket.domain.entity.StockHistory;
import com.misys.stockmarket.exception.service.StockServiceException;
import com.misys.stockmarket.services.StockService;
import com.misys.stockmarket.utility.DateUtils;

public class TestStockHistory {

	private static final Log LOG = LogFactory.getLog(TestStockHistory.class);
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"META-INF\\spring\\applicationContext.xml");
		StockService stockService = (StockService) applicationContext
				.getBean("stockService");

		try {
			List<StockHistory> stockHistoryList = stockService
					.listStockHistory("AAPL",
							DateUtils.getPastMonthFromCurrentDate(6),
							new Date());
			LOG.info("HISTORY SIZE:" + stockHistoryList.size());
			for (StockHistory stockHistory : stockHistoryList) {
				LOG.info(stockHistory);
			}
		} catch (StockServiceException e) {
			LOG.error("Error encountered while testing stock history", e);
		}
	}

}
