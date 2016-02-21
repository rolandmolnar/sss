package domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import junit.framework.Assert;

import org.junit.Test;

import domain.Stock.StockType;
import domain.Trade.TradeType;

public class TradeTest {

	private static Stock getStock() {
		return Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(0.02), 100);
	}

	@Test
	public void validStockTest() {
		Trade.getInstance(getStock(), 97, BigInteger.valueOf(200), TradeType.Buy, LocalDateTime.now());
		Trade.getInstance(getStock(), -2, BigInteger.valueOf(300), TradeType.Sell, LocalDateTime.now());
	}

	@Test
	public void invalidStockTest() {
		try {
			Trade.getInstance(null, 97, BigInteger.valueOf(200), TradeType.Buy, LocalDateTime.now());
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidQuantityTest() {
		try {
			Trade.getInstance(getStock(), 97, BigInteger.valueOf(0), TradeType.Buy, LocalDateTime.now());
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			Trade.getInstance(getStock(), 97, BigInteger.valueOf(-100), TradeType.Buy, LocalDateTime.now());
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidTradeTypeTest() {
		try {
			Trade.getInstance(getStock(), 97, BigInteger.valueOf(200), null, LocalDateTime.now());
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidTimestampTest() {
		try {
			Trade.getInstance(getStock(), 97, BigInteger.valueOf(200), TradeType.Buy, null);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
}