package com.limito.limitoresellproduct.presentation.dto.request;

import java.util.List;
import java.util.UUID;

import com.limito.limitoresellproduct.domain.model.Option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductCreateRequestV1 {

	@NotBlank
	private String productName;

	@NotBlank
	private String brandName;

	@NotNull
	private UUID categoryId;

	@NotNull
	private List<Option> options;
}
