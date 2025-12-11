package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.UUID;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.vo.Option;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductInfosGetResponseV1 {
	private UUID productId;
	private UUID optionId;
	private UUID stockId;
	private String productType;
	private String productName;
	private String brandName;
	private Long sellerId;
	private String productColor;
	private String productSize;
	private int productPrice;

	public static ProductInfosGetResponseV1 create(Product product, Option option, Stock stock) {
		return ProductInfosGetResponseV1.builder()
			.productId(product.getProductId())
			.optionId(option.getOptionId())
			.stockId(stock.getId())
			.productType("RESELL")
			.productName(product.getName())
			.brandName(product.getBrandName())
			.productColor(option.getColor())
			.productSize(option.getSize())
			.productPrice(stock.getPrice())
			.sellerId(stock.getSellerId())
			.build();
	}
}
