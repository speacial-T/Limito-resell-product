package com.limito.limitoresellproduct.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StockCreateRequestV1 {

	@NotNull(message = "재고 상품ID는 필수 입력값입니다.")
	private UUID productId;

	@NotNull(message = "재고 옵션 ID는 필수 입력값입니다.")
	private UUID optionId;

	@NotNull(message = "재고 가격은 필수 입력값입니다.")
	@Min(value = 0, message = "재고 가격은 0이상 이어야 합니다.")
	private int price;
}
