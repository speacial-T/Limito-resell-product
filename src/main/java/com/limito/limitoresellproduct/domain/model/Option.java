package com.limito.limitoresellproduct.domain.model;

import java.util.List;
import java.util.UUID;

import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_resell_product_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID optionId;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "외래키 칼럼명", insertable = false, updatable = false)
	private Product product;

	public Option(
		UUID optionId,
		UUID productId,
		String modelNumber,
		String size,
		String color,
		String thumbnailUrl,
		String details
	) {
		this.optionId = optionId;
		setProductId(productId);
		setModelNumber(modelNumber);
		setSize(size);
		setColor(color);
		setThumbnailUrl(thumbnailUrl);
		this.details = details;
	}

	public static List<Option> validateOptions(List<Option> options) {
		options.stream().forEach(option -> {
			option = new Option(
				option.optionId,
				option.productId,
				option.modelNumber,
				option.size,
				option.color,
				option.thumbnailUrl,
				option.details);
		});
		return options;
	}

	public void setOneOption() {
		if (this.size.isBlank()) {
			this.size = "one size";
		}
		if (this.color.isBlank()) {
			this.color = "one color";
		}
	}

	private void setProductId(UUID productId) {
		if (productId == null) {
			throw new IllegalArgumentException("상품ID는 필수입니다.");
		}
		this.productId = productId;
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
}
