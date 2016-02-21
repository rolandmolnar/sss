package service;

import java.math.BigDecimal;

import domain.Stock;

public interface StockCalculatorService {
	
	void setStock(Stock stock);
	BigDecimal calculateTickerPrice();
	BigDecimal calculateDividendYield();
	BigDecimal calculatePE();
}