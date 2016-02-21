package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import db.TradingDatabase;
import domain.Trade;

public interface TradingDao {

	TradingDatabase getDatabase();
	void insert(Trade trade);
	List<Trade> getTrades(String stockSymbol);
	BigDecimal getStockPrice(String stockSymbol);
	Map<String, BigDecimal> getAllStockPrices();
}