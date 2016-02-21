package dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import junit.framework.Assert;

import org.junit.Test;

import db.TradingDatabase;
import domain.Stock;
import domain.Trade;
import domain.Stock.StockType;
import domain.Trade.TradeType;

public class TradingDaoTest {

	private TradingDatabase tradingDatabase;
	private TradingDao tradingDao;
	private TradingDaoCachedImpl cachedTradingDao;

	private void initContext() {
		tradingDatabase = new TradingDatabase();
		tradingDao = new TradingDaoImpl(tradingDatabase);
		cachedTradingDao = new TradingDaoCachedImpl(tradingDao);
	}

	private static Stock getStockPOP() {
		return Stock.getInstance("POP", StockType.Common, 8, null, 100);
	}

	private static Stock getStockGIN() {
		return Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(0.02), 100);
	}

	@Test
	public void cachingDaoTest() {
		initContext();
		Trade tradePop0 = Trade.getInstance(getStockPOP(), 95, BigInteger.valueOf(2000), TradeType.Buy, LocalDateTime.now());
		cachedTradingDao.insert(tradePop0);
		Assert.assertEquals(0, cachedTradingDao.stockSymbolIndexedCache.size());
		cachedTradingDao.getStockPrice("GIN");
		Assert.assertEquals(0, cachedTradingDao.stockSymbolIndexedCache.size());
		cachedTradingDao.getStockPrice("POP");
		Assert.assertEquals(1, cachedTradingDao.stockSymbolIndexedCache.size());
		Trade tradeGin0 = Trade.getInstance(getStockGIN(), 102, BigInteger.valueOf(500), TradeType.Sell, LocalDateTime.now());
		cachedTradingDao.insert(tradeGin0);
		Assert.assertEquals(1, cachedTradingDao.stockSymbolIndexedCache.size());		
		cachedTradingDao.getStockPrice("POP");
		Assert.assertEquals(1, cachedTradingDao.stockSymbolIndexedCache.size());
		cachedTradingDao.getStockPrice("GIN");
		Assert.assertEquals(2, cachedTradingDao.stockSymbolIndexedCache.size());
		Trade tradeGin1 = Trade.getInstance(getStockGIN(), 99, BigInteger.valueOf(300), TradeType.Buy, LocalDateTime.now());
		cachedTradingDao.insert(tradeGin1);
		Assert.assertEquals(1, cachedTradingDao.stockSymbolIndexedCache.size());
		Trade tradePop1 = Trade.getInstance(getStockPOP(), 98, BigInteger.valueOf(1200), TradeType.Buy, LocalDateTime.now());
		cachedTradingDao.insert(tradePop1);
		Assert.assertEquals(0, cachedTradingDao.stockSymbolIndexedCache.size());
		cachedTradingDao.getAllStockPrices();
		Assert.assertEquals(2, cachedTradingDao.stockSymbolIndexedCache.size());
	}
}