package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.framework.Assert;
import dao.TradingDao;
import dao.TradingDaoImpl;
import db.TradingDatabase;
import domain.Stock;
import domain.Trade;
import domain.Stock.StockType;
import domain.Trade.TradeType;

public class TradeRecorderServiceTest {

	private TradingDatabase tradingDatabase;
	private TradingDao tradingDao;
	private TradeRecorderService tradeRecorderService;

	private void initContext() {
		tradingDatabase = new TradingDatabase();
		tradingDao = new TradingDaoImpl(tradingDatabase);
		tradeRecorderService = new TradeRecorderServiceImpl(tradingDao);
	}

	private static Stock getStockPOP() {
		return Stock.getInstance("POP", StockType.Common, 8, null, 100);
	}

	private static Stock getStockGIN() {
		return Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(0.02), 100);
	}

	@Test
	public void tradeRecordsAreCorrectTest() throws InterruptedException {
		initContext();
		Trade tradePop0 = Trade.getInstance(getStockPOP(), 95, BigInteger.valueOf(2000), TradeType.Buy, LocalDateTime.now());
		tradeRecorderService.recordTrade(tradePop0);
		Trade tradeGin0 = Trade.getInstance(getStockGIN(), 102, BigInteger.valueOf(500), TradeType.Sell, LocalDateTime.now());
		tradeRecorderService.recordTrade(tradeGin0);
		Thread.sleep(1);
		Trade tradeGin1 = Trade.getInstance(getStockGIN(), 99, BigInteger.valueOf(300), TradeType.Buy, LocalDateTime.now());
		tradeRecorderService.recordTrade(tradeGin1);
		Trade tradePop1 = Trade.getInstance(getStockPOP(), 98, BigInteger.valueOf(1200), TradeType.Buy, LocalDateTime.now());
		tradeRecorderService.recordTrade(tradePop1);
		Thread.sleep(1);
		Trade tradePop2 = Trade.getInstance(getStockPOP(), 100, BigInteger.valueOf(1000), TradeType.Sell, LocalDateTime.now());
		tradeRecorderService.recordTrade(tradePop2);

		Map<String, BigDecimal> allPrices = tradingDao.getAllStockPrices();
		Assert.assertEquals(2, allPrices.size());
		List<Trade> trades = tradingDao.getTrades("POP");
		Assert.assertEquals(3, trades.size());
		Assert.assertEquals(tradePop0, trades.get(0));
		Assert.assertEquals(tradePop1, trades.get(1));
		Assert.assertEquals(tradePop2, trades.get(2));
		trades = tradingDao.getTrades("GIN");
		Assert.assertEquals(2, trades.size());
		Assert.assertEquals(tradeGin0, trades.get(0));
		Assert.assertEquals(tradeGin1, trades.get(1));
	}

	@Test
	public void invalidTradeTest() {
		try {
			tradeRecorderService.recordTrade(null);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
}