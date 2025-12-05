package com.limito.limitoresellproduct.domain.vo;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MinimumPriceStock {

	@Column(name = "min_price_stock_id")
	private UUID minimumPriceStockId;

	@Column(name = "min_price_stock_price")
	private BigDecimal minimumPriceStockPrice;
}
