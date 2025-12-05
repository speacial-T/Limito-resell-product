package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import com.limito.limitoresellproduct.domain.model.Option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
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
	private List<Option> options;
}
