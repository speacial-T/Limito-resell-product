package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import org.springframework.data.web.PagedModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductReadResponseV1 {

	private UUID categoryId;
	private PagedModel<ProductReadRes> products;

	public record ProductReadRes(
		UUID productId,
		String productName,
		String brandName,
		List<OptionReadRes> options
	) {
	}

	public record OptionReadRes(
		UUID optionId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details,
		MinStockReadRes minStock
	) {
	}

	public record MinStockReadRes(
		UUID stockId,
		int price
	) {
	}
}
