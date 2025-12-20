package com.limito.limitoresellproduct.domain.vo;

import java.util.Comparator;
import java.util.List;

import com.limito.limitoresellproduct.domain.model.Stock;

public class Stocks {
	private List<Stock> stocks;

	public static Stock calculateMinStock(List<Stock> stocks) {
		Stock minStock = stocks.stream()
			.filter(stock -> !stock.isDeleted())
			.filter(stock -> !stock.isSoldOut())
			.min(Comparator.comparing(Stock::getPrice))
			.orElse(null);

		return minStock;
	}
}
