package com.limito.limitoresellproduct.presentation.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductCreateRequestV1 {

	@NotNull
	@Valid
	private ProductInfo productInfo;

	@NotNull
	private String modelNumber;

	private String color;

	@NotNull
	private String thumbnailUrl;

	private String details;

	private List<OptionRequestV1> options;

	public record OptionRequestV1(
		String size
	) {
	}

	public record ProductInfo(
		@NotBlank(message = "상품명은 필수 입력값입니다.") String productName,
		@NotBlank(message = "브랜드명은 필수 입력값입니다.") String brandName,
		@NotNull(message = "카테고리ID는 필수 입력값입니다.") UUID categoryId
	) {
	}
}
