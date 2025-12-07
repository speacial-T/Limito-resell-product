package com.limito.limitoresellproduct.presentation.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductCreateRequestV1 {

	@NotBlank(message = "상품명은 필수 입력값입니다.")
	private String productName;

	@NotBlank(message = "브랜드명은 필수 입력값입니다.")
	private String brandName;

	@NotNull(message = "카테고리ID는 필수 입력값입니다.")
	private UUID categoryId;

	@NotNull(message = "상품의 옵션은 적어도 하나 있어야 합니다.")
	private List<OptionRequestV1> options;

	public record OptionRequestV1(
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details
	) {
	}
}
