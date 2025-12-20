package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCreateResponseV1 {

	private ProductInfoResponse productInfo;

	private String modelNumber;

	private String color;

	private String thumbnailUrl;

	private String details;

	private List<OptionResponseV1> options;

	public record ProductInfoResponse(
		UUID productId,
		String productName,
		String brandName,
		UUID categoryId
	) {
	}

	public record OptionResponseV1(
		UUID optionId,
		UUID modelId,
		String size,
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
