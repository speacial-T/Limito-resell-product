package com.limito.limitoresellproduct.domain.model;

import java.util.List;
import java.util.UUID;

import com.limito.common.audit.BaseEntity;
import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Product;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_resell_models")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Model extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID modelId;

	@Column(name = "model_number", nullable = false, length = 50)
	private String modelNumber;

	@Column(name = "color", nullable = false, length = 50)
	private String color;

	@Column(name = "thumbnail_url", nullable = false)
	private String thumbnailUrl;

	@Column(name = "details", columnDefinition = "TEXT")
	private String details;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@OneToMany
	private List<Optiono> options;

	public Model(
		String modelNumber,
		String color,
		String thumbnailUrl,
		String details,
		Product product,
		List<Optiono> options
	) {
		setModelNumber(modelNumber);
		setColor(color);
		setThumbnailUrl(thumbnailUrl);
		this.details = details;
		setProduct(product);
		setOptions(options);
	}

	public void changeMinimumPriceStock(UUID optionId, MinimumPriceStock newStock) {
		this.validOptionActive(optionId);
		Optiono option = this.getOption(optionId);
		option.changeMinimumPriceStock(newStock);
	}

	public Optiono getOption(UUID optionId) {
		return options.stream()
			.filter(option -> option.isEqualId(optionId))
			.findFirst()
			.orElse(null);
	}

	private void setModelNumber(String modelNumber) {
		if (modelNumber == null || modelNumber.isBlank()) {
			throw AppException.of(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 모델번호는 필수 입력값입니다."
			);
		}
		this.modelNumber = modelNumber;
	}

	private void setColor(String color) {
		if (color == null || color.isBlank()) {
			color = "one color";
		}
		this.color = color;
	}

	private void setThumbnailUrl(String thumbnailUrl) {
		if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
			throw AppException.of(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 대표 이미지는 필수 입력값입니다."
			);
		}
		this.thumbnailUrl = thumbnailUrl;
	}

	private void setProduct(Product product) {
		if (product == null) {
			throw AppException.of(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 상품정보는 필수 입력값입니다."
			);
		}
		this.product = product;
	}

	private void setOptions(List<Optiono> options) {
		if (options == null) {
			throw AppException.of(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 옵션은 적어도 하나 있어야 합니다."
			);
		}
		if (options.size() == 1) {
			options.get(0).setOneOption();
		}
		this.options = options;
	}

	private void validOptionActive(UUID optionId) {
		Optiono option = this.getOption(optionId);
		if (option == null) {
			throw AppException.of(ProductErrorCode.WRONG_OPTION_ID);
		}

		option.checkActive();
	}
}
