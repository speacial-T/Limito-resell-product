package com.limito.limitoresellproduct.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StockReduceRequest {
	@NotNull(message = "상품 ID는 필수 입력값입니다.")
	private UUID productId;

	@NotNull(message = "옵션 ID는 필수 입력값입니다.")
	private UUID optionId;

	@NotNull(message = "재고 ID는 필수 입력값입니다.")
	private UUID stockId;
}
