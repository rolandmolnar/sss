package service;

import dao.TradingDao;
import domain.Trade;

public class TradeRecorderServiceImpl implements TradeRecorderService {

	private final TradingDao tradingDao;

	public TradeRecorderServiceImpl(TradingDao tradingDao) {
		this.tradingDao = tradingDao;
	}

	@Override
	public void recordTrade(Trade trade) {
		if (trade == null) {
			throw new IllegalArgumentException("Trade cannot be null.");
		}
		tradingDao.insert(trade);
	}
}