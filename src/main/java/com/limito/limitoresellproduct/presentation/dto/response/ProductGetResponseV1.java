package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductGetResponseV1 {

	private UUID productId;
	private String productName;
	private String brandName;
	private List<ProductGetResponseOption> options;

	private record ProductGetResponseOption(
		UUID optionId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details,
		ProductGetResponseMinStock minStock
	) {
	}

	private record ProductGetResponseMinStock(
		UUID stockId,
		int price
	) {
	}

	public static ProductGetResponseV1 of(Product product) {

		List<ProductGetResponseOption> mappedOptions = product.getOptions().stream()
			.map(option -> {

				MinimumPriceStock stock = option.getMinimumPriceStock();  // 엔티티 명은 상황에 맞게 변경

				ProductGetResponseMinStock minStockRes = null;
				if (stock != null) {
					minStockRes = new ProductGetResponseMinStock(
						stock.getMinimumPriceStockId(),
						stock.getMinimumPriceStockPrice()
					);
				}

				return new ProductGetResponseOption(
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

		return new ProductGetResponseV1(
			product.getProductId(),
			product.getName(),
			product.getBrandName(),
			mappedOptions
		);
	}
}
