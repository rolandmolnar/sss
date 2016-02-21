package service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import junit.framework.Assert;

import org.junit.Test;

import dao.TradingDao;
import dao.TradingDaoCachedImpl;
import dao.TradingDaoImpl;
import db.TradingDatabase;
import domain.Stock;
import domain.Trade;
import domain.Stock.StockType;
import domain.Trade.TradeType;

public class TradeCalculatorServiceTest {

	private TradingDatabase tradingDatabase;
	private TradingDao cachedTradingDao;
	private TradeRecorderService tradeRecorderService;
	private TradeCalculatorService tradeCalculatorService;

	private void initContext() {
		tradingDatabase = new TradingDatabase();
		TradingDao tradingDao = new TradingDaoImpl(tradingDatabase);
		cachedTradingDao = new TradingDaoCachedImpl(tradingDao);
		tradeRecorderService = new TradeRecorderServiceImpl(cachedTradingDao);
		tradeCalculatorService = new TradeCalculatorServiceImpl(cachedTradingDao);
	}

	private static Stock getStockPOP() {
		return Stock.getInstance("POP", StockType.Common, 8, null, 100);
	}

	private static Stock getStockGIN() {
		return Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(0.02), 100);
	}

	private static Stock getStockALE() {
		return Stock.getInstance("ALE", StockType.Common, 23, null, 60);
	}

	private void initDB() {
		try {
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
			Trade tradeAle0 = Trade.getInstance(getStockALE(), 67, BigInteger.valueOf(750), TradeType.Buy, LocalDateTime.now());
			tradeRecorderService.recordTrade(tradeAle0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calculateGBCEAllShareIndexTest() {
		initContext();
		initDB();
		BigDecimal geomerticMean = tradeCalculatorService.calculateGBCEAllShareIndex();
		Assert.assertEquals(BigDecimal.valueOf(86.89), geomerticMean);
	}

	@Test
	public void calculateGeometricMeanFailureTest() {
		initContext();
		try {
			tradeCalculatorService.calculateGBCEAllShareIndex();
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
}