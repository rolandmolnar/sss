package domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Trade { // Immutable

	private final Stock stock;
	private final int price;
	private final BigInteger quantity; // Fractional quantities are not possible
	private final TradeType tradeType;
	private final LocalDateTime timestamp;

	public static enum TradeType {
		Buy, Sell
	}

	private Trade(Stock stock, int price, BigInteger quantity, TradeType tradeType, LocalDateTime timestamp) {
		this.stock = stock;
		this.price = price;
		this.quantity = quantity;
		this.tradeType = tradeType;
		this.timestamp = timestamp;
		validate();
	}

	private void validate() {
		if (stock == null) {
			throw new IllegalArgumentException("Trade stock cannot be null.");
		}
		// Price can be 0 or negative
		if (quantity == null || quantity.compareTo(BigInteger.ZERO) <= 0) {
			throw new IllegalArgumentException("Trade quantity must be greater than 0.");
		}
		if (tradeType == null) {
			throw new IllegalArgumentException("Trade type cannot be null.");
		}
		if (timestamp == null) {
			throw new IllegalArgumentException("Trade timestamp cannot be null.");
		}
	}

	public static Trade getInstance(Stock stock, int price, BigInteger quantity, TradeType tradeType, LocalDateTime timestamp) {
		return new Trade(stock, price, quantity, tradeType, timestamp);
	}

	public Stock getStock() {
		return stock;
	}

	public int getPrice() {
		return price;
	}

	public BigInteger getQuantity() {
		return quantity;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "Trade [stock=" + stock + ", price=" + price + ", quantity="
				+ quantity + ", tradeType=" + tradeType + ", timestamp="
				+ timestamp + "]";
	}
}