package com.limito.limitoresellproduct.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductInfosGetRequestV1 {
	@NotNull
	private UUID productId;

	@NotNull
	private UUID optionId;

	@NotNull
	private UUID stockId;
}
