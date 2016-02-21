package db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import domain.Trade;

public class TradingDatabase {

	private final Map<String, List<Trade>> stockSymbolIndexedDB = new ConcurrentHashMap<String, List<Trade>>();

	public void insert(Trade trade) {
		String stockSymbol = trade.getStock().getSymbol();
		if (!stockSymbolIndexedDB.containsKey(stockSymbol)) {
			stockSymbolIndexedDB.put(stockSymbol, new CopyOnWriteArrayList<Trade>());
		}
		stockSymbolIndexedDB.get(stockSymbol).add(trade);
	}

	public Map<String, List<Trade>> getAllTradesIndexed() {
		return stockSymbolIndexedDB;
	}

	@Override
	public String toString() {
		return "TradingDatabase [stockSymbolIndexedDB=" + stockSymbolIndexedDB + "]";
	}
}