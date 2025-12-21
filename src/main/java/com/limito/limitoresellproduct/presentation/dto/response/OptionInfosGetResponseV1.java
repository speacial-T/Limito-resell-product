package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OptionInfosGetResponseV1 {
	private UUID optionId;
	private UUID stockId;
	private UUID productId;
	private String productType;
	private String productName;
	private String productColor;
	private String productSize;
	private int productPrice;
	private String brandName;
	private String thumbnailUrl;
	private Long sellerId;
}
