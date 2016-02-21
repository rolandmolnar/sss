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

public class StockCalculatorServiceTest {

	private TradingDatabase tradingDatabase;
	private TradingDao cachedTradingDao;
	private TradeRecorderService tradeRecorderService;
	private StockCalculatorService stockCalculatorService;

	private void initContext() {
		tradingDatabase = new TradingDatabase();
		TradingDao tradingDao = new TradingDaoImpl(tradingDatabase);
		cachedTradingDao = new TradingDaoCachedImpl(tradingDao);
		tradeRecorderService = new TradeRecorderServiceImpl(cachedTradingDao);
		stockCalculatorService = new StockCalculatorServiceImpl(cachedTradingDao);
	}

	private static Stock getStockPOP() {
		return Stock.getInstance("POP", StockType.Common, 8, null, 100);
	}

	private static Stock getStockGIN() {
		return Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(0.02), 100);
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calculateTickerPriceTest() {
		initContext();
		initDB();
		Stock stock = getStockPOP();
		stockCalculatorService.setStock(stock);
		BigDecimal tickerPrice = stockCalculatorService.calculateTickerPrice();
		Assert.assertEquals(BigDecimal.valueOf(97.05), tickerPrice);
		// Obsolete trade does not change the calculation result
		Trade tradePop3 = Trade.getInstance(getStockPOP(), 104, BigInteger.valueOf(1350), TradeType.Sell, LocalDateTime.now().minusMinutes(18));
		tradeRecorderService.recordTrade(tradePop3);
		tickerPrice = stockCalculatorService.calculateTickerPrice();
		Assert.assertEquals(BigDecimal.valueOf(97.05), tickerPrice);
		// Recent trade does change the calculation result
		Trade tradePop4 = Trade.getInstance(getStockPOP(), 97, BigInteger.valueOf(1350), TradeType.Buy, LocalDateTime.now().minusMinutes(2));
		tradeRecorderService.recordTrade(tradePop4);
		tickerPrice = stockCalculatorService.calculateTickerPrice();
		Assert.assertNotSame(BigDecimal.valueOf(97.05), tickerPrice);

		stock = getStockGIN();
		stockCalculatorService.setStock(stock);
		tickerPrice = stockCalculatorService.calculateTickerPrice();
		Assert.assertEquals(BigDecimal.valueOf(100.88), tickerPrice);
	}

	@Test
	public void calculateDividendYieldTest() {
		initContext();
		initDB();
		Stock stock = getStockPOP();
		stockCalculatorService.setStock(stock);
		BigDecimal divYield = stockCalculatorService.calculateDividendYield();
		Assert.assertEquals(BigDecimal.valueOf(0.0824), divYield);

		stock = getStockGIN();
		stockCalculatorService.setStock(stock);
		divYield  = stockCalculatorService.calculateDividendYield();
		Assert.assertEquals(BigDecimal.valueOf(0.0198), divYield);
	}

	@Test
	public void calculateTickerPETest() {
		initContext();
		initDB();
		Stock stock = getStockPOP();
		stockCalculatorService.setStock(stock);
		BigDecimal pe = stockCalculatorService.calculatePE();
		Assert.assertEquals(BigDecimal.valueOf(11.78), pe);

		stock = getStockGIN();
		stockCalculatorService.setStock(stock);
		pe  = stockCalculatorService.calculatePE();
		Assert.assertEquals(BigDecimal.valueOf(50.95), pe);
	}
}