package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCreateResponseV1 {

	@NotNull
	private UUID productId;

	@NotBlank
	private String productName;

	@NotBlank
	private String brandName;

	@NotNull
	private UUID categoryId;

	@NotNull
	private List<OptionResponseV1> options;

	public record OptionResponseV1(
		UUID optionId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details,
		MinStockResponse minStock
	) {
	}

	public record MinStockResponse(
		UUID stockId,
		int price
	) {
	}
}
