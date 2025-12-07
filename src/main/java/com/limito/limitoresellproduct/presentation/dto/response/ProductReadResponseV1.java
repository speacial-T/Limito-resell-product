package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductReadResponseV1 {

	private UUID categoryId;
	private Page<ProductReadRes> products;

	private record ProductReadRes(
		UUID productId,
		String productName,
		String brandName,
		List<OptionReadRes> options
	) {
	}

	private record OptionReadRes(
		UUID optionId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details,
		MinStockReadRes minStock
	) {
	}

	private record MinStockReadRes(
		UUID stockId,
		int price
	) {
	}

	public static ProductReadResponseV1 of(UUID categoryId, Page<Product> products) {

		Page<ProductReadRes> mapped = products.map(product -> {

			List<OptionReadRes> mappedOptions = product.getOptions().stream()
				.map(option -> {

					MinimumPriceStock stock = option.getMinimumPriceStock();  // 엔티티 명은 상황에 맞게 변경

					MinStockReadRes minStockRes = null;
					if (stock != null) {
						minStockRes = new MinStockReadRes(
							stock.getMinimumPriceStockId(),
							stock.getMinimumPriceStockPrice()
						);
					}

					return new OptionReadRes(
						option.getOptionId(),
						option.getModelNumber(),
						option.getSize(),
						option.getColor(),
						option.getThumbnailUrl(),
						option.getDetails(),
						minStockRes
					);
				})
				.toList();

			return new ProductReadRes(
				product.getProductId(),
				product.getName(),
				product.getBrandName(),
				mappedOptions
			);
		});

		return ProductReadResponseV1.builder()
			.categoryId(categoryId)
			.products(mapped)
			.build();
	}
}
