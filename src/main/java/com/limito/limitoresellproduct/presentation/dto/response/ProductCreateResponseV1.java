package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCreateResponseV1 {
	private UUID productId;
	private String productName;
	private String brandName;
	private UUID categoryId;
	private List<OptionResponseV1> options;

	public record OptionResponseV1(
		UUID optionId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details,
		boolean inStock,
		MinStockResponse minStock
	) {
	}

	public record MinStockResponse(
		UUID stockId,
		int price
	) {
	}
}
