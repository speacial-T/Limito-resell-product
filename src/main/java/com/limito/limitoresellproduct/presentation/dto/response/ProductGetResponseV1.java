package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductGetResponseV1 {

	private UUID productId;
	private String productName;
	private String brandName;
	private List<ProductGetResponseOption> options;

	public record ProductGetResponseOption(
		UUID optionId,
		String size,
		ProductGetResponseMinStock minStock
	) {
	}

	public record ProductGetResponseMinStock(
		UUID stockId,
		int price
	) {
	}
}
