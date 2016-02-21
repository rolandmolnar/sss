package domain;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import domain.Stock.StockType;

public class StockTest {

	@Test
	public void validStockTest() {
		Stock.getInstance("TEA", StockType.Common, 0, null, 100);
		Stock.getInstance("POP", StockType.Common, 8, null, 100);
		Stock.getInstance("ALE", StockType.Common, 23, null, 60);
		Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(0.02), 100);
		Stock.getInstance("JOE", StockType.Common, 13, null, 250);
		Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.ZERO, 100);
	}

	@Test
	public void invalidStockSymbolTest() {
		try {
			Stock.getInstance(null, StockType.Common, 8, null, 100);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			Stock.getInstance("", StockType.Common, 8, null, 100);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidStockTypeTest() {
		try {
			Stock.getInstance("POP", null, 8, null, 100);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidLastDividendTest() {
		try {
			Stock.getInstance("POP", StockType.Common, -4, null, 100);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidFixedDividendTest() {
		try {
			Stock.getInstance("GIN", StockType.Preferred, 8, null, 100);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			Stock.getInstance("GIN", StockType.Preferred, 8, BigDecimal.valueOf(-0.02), 100);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invalidParValueTest() {
		try {
			Stock.getInstance("POP", StockType.Common, 8, null, 0);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			Stock.getInstance("POP", StockType.Common, 8, null, -2);
			Assert.fail();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
}