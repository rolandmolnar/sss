package dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import db.TradingDatabase;
import domain.Trade;

public class TradingDaoCachedImpl implements TradingDao {

	final Map<String, BigDecimal> stockSymbolIndexedCache = new ConcurrentHashMap<String, BigDecimal>();

	private final TradingDao tradingDao;

	public TradingDaoCachedImpl(TradingDao tradingDao) {
		this.tradingDao = tradingDao;
	}

	@Override
	public TradingDatabase getDatabase() {
		return tradingDao.getDatabase();
	}

	@Override
	public void insert(Trade trade) {
		stockSymbolIndexedCache.remove(trade.getStock().getSymbol());
		tradingDao.insert(trade);
	}

	@Override
	public List<Trade> getTrades(String stockSymbol) {
		return tradingDao.getTrades(stockSymbol); // Not cached
	}

	@Override
	public BigDecimal getStockPrice(String stockSymbol) {
		if (stockSymbolIndexedCache.containsKey(stockSymbol)) {
			return stockSymbolIndexedCache.get(stockSymbol);
		}
		BigDecimal stockPrice = tradingDao.getStockPrice(stockSymbol);
		if (stockPrice != null) {
			stockSymbolIndexedCache.put(stockSymbol, stockPrice);
		}
		return stockPrice;
	}

	@Override
	public Map<String, BigDecimal> getAllStockPrices() {
		Map<String, BigDecimal> stockPrices = new HashMap<String, BigDecimal>();
		for (String stockSymbol : tradingDao.getDatabase().getAllTradesIndexed().keySet()) {
			BigDecimal avgPrice = getStockPrice(stockSymbol);
			stockPrices.put(stockSymbol, avgPrice);
		}
		return stockPrices;
	}
}