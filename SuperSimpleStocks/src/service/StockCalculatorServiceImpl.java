package service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import dao.TradingDao;
import domain.Stock;
import domain.Stock.StockType;

public class StockCalculatorServiceImpl implements StockCalculatorService {

	private Stock stock;

	private final TradingDao tradingDao;

	public StockCalculatorServiceImpl(TradingDao tradingDao) {
		this.tradingDao = tradingDao;
	}

	public Stock getStock() {
		return stock;
	}

	@Override
	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@Override
	public BigDecimal calculateTickerPrice() {
		BigDecimal tickerPrice = tradingDao.getStockPrice(stock.getSymbol());
		if (tickerPrice == null) {
			throw new RuntimeException("Ticker price is not available");
		}
		return tickerPrice;
	}

	@Override
	public BigDecimal calculateDividendYield() {
		BigDecimal tickerPrice = calculateTickerPrice();
		return calculateDividendYield(tickerPrice);
	}

	private BigDecimal calculateDividendYield(BigDecimal tickerPrice) {
		if (stock.getStockType() == StockType.Common) {
			return new BigDecimal(stock.getLastDividend()).divide(tickerPrice, 4, RoundingMode.HALF_UP);
		} else if (stock.getStockType() == StockType.Preferred) {
			return stock.getFixedDividend()
					.multiply(new BigDecimal(stock.getParValue()))
					.divide(tickerPrice, 4, RoundingMode.HALF_UP);
		}
		throw new IllegalArgumentException("Unknown stock type: "+stock);
	}

	@Override
	public BigDecimal calculatePE() {
		BigDecimal tickerPrice = calculateTickerPrice();
		// Dividend percentage value is used
		BigDecimal dividend = calculateDividendYield(tickerPrice).multiply(BigDecimal.valueOf(100));
		if (dividend.compareTo(BigDecimal.ZERO) == 0) {
			throw new RuntimeException("No P/E ratio. Dividend is 0 for stock: "+stock);
		}
		return tickerPrice.divide(dividend, 2, RoundingMode.HALF_UP);
	}
}