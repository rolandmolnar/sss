package dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.TradingDatabase;
import domain.Trade;

public class TradingDaoImpl implements TradingDao {

	private final TradingDatabase db;

	public TradingDaoImpl(TradingDatabase db) {
		this.db = db;
	}

	@Override
	public TradingDatabase getDatabase() {
		return db;
	}

	@Override
	public void insert(Trade trade) {
		db.insert(trade);
	}

	@Override
	public List<Trade> getTrades(String stockSymbol) {
		Map<String, List<Trade>> dbContent = db.getAllTradesIndexed();
		if (!dbContent.containsKey(stockSymbol)) {
			return null;
		}
		return dbContent.get(stockSymbol);
	}

	@Override
	public BigDecimal getStockPrice(String stockSymbol) {
		Map<String, List<Trade>> dbContent = db.getAllTradesIndexed();
		if (!dbContent.containsKey(stockSymbol)) {
			return null;
		}
		BigInteger tradeSum = BigInteger.ZERO;
		BigInteger quantitySum = BigInteger.ZERO;
		for (Trade trade : dbContent.get(stockSymbol)) {
			// Trades are not coming in timestamp order
			if (trade.getTimestamp().compareTo(LocalDateTime.now().minusMinutes(15)) > 0) {
				BigInteger tradeValue = BigInteger.valueOf(trade.getPrice()).multiply(trade.getQuantity());
				tradeSum = tradeSum.add(tradeValue);
				quantitySum = quantitySum.add(trade.getQuantity());
			}
		}
		if (quantitySum.compareTo(BigInteger.ZERO) == 0) {
			return null;
		}
		BigDecimal avgPrice = new BigDecimal(tradeSum).divide(new BigDecimal(quantitySum), 2, RoundingMode.HALF_UP);
		return avgPrice;
	}

	@Override
	public Map<String, BigDecimal> getAllStockPrices() {
		Map<String, BigDecimal> stockPrices = new HashMap<String, BigDecimal>();
		for (String stockSymbol : db.getAllTradesIndexed().keySet()) {
			BigDecimal avgPrice = getStockPrice(stockSymbol);
			stockPrices.put(stockSymbol, avgPrice);
		}
		return stockPrices;
	}
}