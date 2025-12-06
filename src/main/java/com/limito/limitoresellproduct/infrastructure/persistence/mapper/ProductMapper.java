package com.limito.limitoresellproduct.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;

@Component
public class ProductMapper {

	public static ProductCreateResponseV1 toDto(Product product) {
		return ProductCreateResponseV1.builder()
			.productId(product.getProductId())
			.productName(product.getName())
			.brandName(product.getBrandName())
			.categoryId(product.getCategoryId())
			.options(product.getOptions().stream()
				.map(o -> new ProductCreateResponseV1.OptionResponseV1(
					o.getOptionId(),
					o.getModelNumber(),
					o.getSize(),
					o.getColor(),
					o.getThumbnailUrl(),
					o.getDetails(),
					toDto(o.getMinimumPriceStock())
				))
				.toList())
			.build();
	}

	public static ProductCreateResponseV1.MinStockResponse toDto(MinimumPriceStock stock) {
		return new ProductCreateResponseV1.MinStockResponse(
			stock.getMinimumPriceStockId(),
			stock.getMinimumPriceStockPrice()
		);
	}
}
