package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockCreateResponseV1 {

	private UUID stockId;

	private UUID optionId;

	private int price;

	private boolean soldOut;

	private Long sellerId;
}
