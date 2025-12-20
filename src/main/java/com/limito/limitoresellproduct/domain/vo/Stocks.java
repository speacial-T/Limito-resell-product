package com.limito.limitoresellproduct.domain.vo;

import java.util.Comparator;
import java.util.List;

import com.limito.limitoresellproduct.domain.model.Stock;

public class Stocks {
	private List<Stock> stocks;

	public static MinimumPriceStock calculateMinStock(List<Stock> stocks) {
		MinimumPriceStock minStock = stocks.stream()
			.filter(stock -> !stock.isDeleted())
			.filter(stock -> !stock.isSoldOut())
			.min(Comparator.comparing(Stock::getPrice))
			.map(stock -> new MinimumPriceStock(stock.getId(), stock.getPrice(), stock.getSellerId()))
			.orElse(new MinimumPriceStock(null, null, null));

		return minStock;
	}
}
