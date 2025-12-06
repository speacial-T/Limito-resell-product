package com.limito.limitoresellproduct.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;

import jakarta.validation.Valid;

@Component
public class ProductMapper {

	public static Product toEntity(@Valid ProductCreateRequestV1 request) {
		List<Option> options = request.getOptions().stream()
			.map(optionRequest -> new Option(
				null,
				optionRequest.modelNumber(),
				optionRequest.size(),
				optionRequest.color(),
				optionRequest.thumbnailUrl(),
				optionRequest.details(),
				null)
			)
			.toList();
		Product product = new Product(
			request.getProductName(),
			request.getBrandName(),
			request.getCategoryId(),
			options
		);
		return product;
	}

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
