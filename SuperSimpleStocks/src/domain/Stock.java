package domain;

import java.math.BigDecimal;

public class Stock { // Immutable

	private final String symbol;
	private final StockType stockType;
	private final int lastDividend;
	private final BigDecimal fixedDividend;
	private final int parValue;

	public static enum StockType {
		Common, Preferred
	}

	private Stock(String symbol, StockType stockType, int lastDividend, BigDecimal fixedDividend, int parValue) {
		this.symbol = symbol;
		this.stockType = stockType;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
		validate();
	}

	private void validate() {
		if (symbol == null || symbol.isEmpty()) {
			throw new IllegalArgumentException("Stock symbol cannot be empty.");
		}
		if (stockType == null) {
			throw new IllegalArgumentException("Stock type cannot be null.");
		}
		if (lastDividend < 0) {
			throw new IllegalArgumentException("Last dividend cannot be negative.");
		}
		if (fixedDividend == null || fixedDividend.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Fixed dividend cannot be negative.");
		}
		if (parValue <= 0) {
			throw new IllegalArgumentException("Par value must be greater than 0.");
		}
	}

	public static Stock getInstance(String symbol, StockType stockType, int lastDividend, BigDecimal fixedDividend, int parValue) {
		if (stockType == StockType.Common) {
			return new Stock(symbol, stockType, lastDividend, BigDecimal.ZERO, parValue);
		}
		return new Stock(symbol, stockType, lastDividend, fixedDividend, parValue);
	}

	public String getSymbol() {
		return symbol;
	}

	public StockType getStockType() {
		return stockType;
	}

	public int getLastDividend() {
		return lastDividend;
	}

	public BigDecimal getFixedDividend() {
		return fixedDividend;
	}

	public int getParValue() {
		return parValue;
	}

	@Override
	public String toString() {
		return "Stock [symbol=" + symbol + ", stockType=" + stockType
				+ ", lastDividend=" + lastDividend + ", fixedDividend="
				+ fixedDividend + ", parValue=" + parValue + "]";
	}
}