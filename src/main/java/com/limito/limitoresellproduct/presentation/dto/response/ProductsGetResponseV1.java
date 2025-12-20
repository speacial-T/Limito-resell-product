package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.UUID;

import org.springframework.data.web.PagedModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductsGetResponseV1 {
	private UUID categoryId;
	private String category;
	private PagedModel<ProductGetResponse> products;

	@Builder
	public record ProductGetResponse(
		UUID productId,
		String productName,
		String brandName,
		UUID modelId,
		String modelNumber,
		String color,
		String thumbnailUrl,
		String details
	) {
	}
}
