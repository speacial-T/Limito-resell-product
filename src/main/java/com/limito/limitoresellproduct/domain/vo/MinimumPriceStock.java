package com.limito.limitoresellproduct.domain.vo;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MinimumPriceStock {

	@Column(name = "min_price_stock_id")
	private UUID minimumPriceStockId;

	@Column(name = "min_price_stock_price")
	private int minimumPriceStockPrice;

	public MinimumPriceStock(UUID stockId, Integer price) {
		this.minimumPriceStockId = stockId;
		setPrice(price);
	}

	private void setPrice(Integer price) {
		if (price == null) {
			price = -1;
		}
		this.minimumPriceStockPrice = price;
	}
}
