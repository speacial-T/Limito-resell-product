package com.limito.limitoresellproduct.domain.vo;

import java.util.UUID;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Option {

	@Column(name = "option_id")
	private UUID optionId;

	@Column(name = "model_number", nullable = false, length = 50)
	private String modelNumber;

	@Column(name = "size", nullable = false, length = 10)
	private String size;

	@Column(name = "color", nullable = false, length = 50)
	private String color;

	@Column(name = "thumbnail_url", nullable = false)
	private String thumbnailUrl;

	@Column(name = "details", columnDefinition = "TEXT")
	private String details;

	@Embedded
	private MinimumPriceStock minimumPriceStock;

	public Option(
		UUID optionId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details,
		MinimumPriceStock minimumPriceStock
	) {
		setOptionId(optionId);
		setModelNumber(modelNumber);
		setSize(size);
		setColor(color);
		setThumbnailUrl(thumbnailUrl);
		this.details = details;
		setMinimumPriceStock(minimumPriceStock);
	}

	public void setOneOption() {
		if (this.size.isBlank()) {
			this.size = "one size";
		}
		if (this.color.isBlank()) {
			this.color = "one color";
		}
	}

	private void setOptionId(UUID optionId) {
		if (optionId == null) {
			optionId = UUID.randomUUID();
		}
		this.optionId = optionId;
	}

	private void setModelNumber(String modelNumber) {
		if (modelNumber == null || modelNumber.isBlank()) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 모델번호는 필수 입력값입니다."
			);
		}
		this.modelNumber = modelNumber;
	}

	private void setSize(String size) {
		if (size == null || size.isBlank()) {
			size = "one size";
		}
		this.size = size;
	}

	private void setColor(String color) {
		if (color == null || color.isBlank()) {
			color = "one color";
		}
		this.color = color;
	}

	private void setThumbnailUrl(String thumbnailUrl) {
		if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 대표 이미지는 필수 입력값입니다."
			);
		}
		this.thumbnailUrl = thumbnailUrl;
	}

	private void setMinimumPriceStock(MinimumPriceStock stock) {
		if (stock == null) {
			stock = new MinimumPriceStock(null, null);
		}
		this.minimumPriceStock = stock;
	}

	public void changeMinimumPriceStock(MinimumPriceStock newStock) {
		if (this.minimumPriceStock == null) {
			this.minimumPriceStock = new MinimumPriceStock(null, null);
		}
		if (newStock == null) {
			newStock = new MinimumPriceStock(null, null);
		}
		if (this.minimumPriceStock.getMinimumPriceStockPrice() < 0) {
			this.minimumPriceStock = newStock;
		}
		if (this.minimumPriceStock.getMinimumPriceStockPrice() > newStock.getMinimumPriceStockPrice()) {
			this.minimumPriceStock = newStock;
		}
	}
}
