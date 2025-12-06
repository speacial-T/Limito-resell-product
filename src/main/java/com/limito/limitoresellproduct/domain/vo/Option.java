package com.limito.limitoresellproduct.domain.vo;

import java.util.List;
import java.util.UUID;

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

	public static List<Option> validateOptions(List<Option> options) {
		List<Option> newOptions = options.stream()
			.map(option -> new Option(
				option.getOptionId(),
				option.getModelNumber(),
				option.getSize(),
				option.getColor(),
				option.getThumbnailUrl(),
				option.getDetails(),
				option.getMinimumPriceStock()
			))
			.toList();
		return newOptions;
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
			throw new IllegalArgumentException("모델번호는 필수입니다.");
		}
		this.modelNumber = modelNumber;
	}

	private void setSize(String size) {
		if (size == null || size.isBlank()) {
			throw new IllegalArgumentException("사이즈는 필수입니다.");
		}
		this.size = size;
	}

	private void setColor(String color) {
		if (color == null || color.isBlank()) {
			throw new IllegalArgumentException("색상는 필수입니다.");
		}
		this.color = color;
	}

	private void setThumbnailUrl(String thumbnailUrl) {
		if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
			throw new IllegalArgumentException("대표 이미지는 필수입니다.");
		}
		this.thumbnailUrl = thumbnailUrl;
	}

	private void setMinimumPriceStock(MinimumPriceStock stock) {
		if (stock == null) {
			stock = new MinimumPriceStock(null, null);
		}
		this.minimumPriceStock = new MinimumPriceStock(
			stock.getMinimumPriceStockId(),
			stock.getMinimumPriceStockPrice()
		);
	}
}
