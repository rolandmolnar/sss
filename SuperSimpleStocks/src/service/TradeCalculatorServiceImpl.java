package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import dao.TradingDao;

public class TradeCalculatorServiceImpl implements TradeCalculatorService{

	private final TradingDao tradingDao;

	public TradeCalculatorServiceImpl(TradingDao tradingDao) {
		this.tradingDao = tradingDao;
	}

	@Override
	public BigDecimal calculateGBCEAllShareIndex() {
		Map<String, BigDecimal> allPrices = tradingDao.getAllStockPrices();
		if (allPrices.isEmpty()) {
			throw new RuntimeException("No stock prices found.");
		}
		BigDecimal priceProduct = BigDecimal.ONE;
		for (BigDecimal price : allPrices.values()) {
			priceProduct = priceProduct.multiply(price);
		}
		double mean = Math.pow(priceProduct.doubleValue(), 1/(double) allPrices.size());
		return new BigDecimal(mean).setScale(2, RoundingMode.HALF_UP);
	}
}