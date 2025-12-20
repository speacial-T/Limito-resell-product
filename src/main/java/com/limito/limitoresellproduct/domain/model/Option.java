package com.limito.limitoresellproduct.domain.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.limito.common.exception.AppException;
import com.limito.common.security.audit.BaseEntity;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Option extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID optionId;

	@Column(name = "model_id")
	private UUID modelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_id", insertable = false, updatable = false)
	@JsonIgnore
	private Model model;

	@Column(name = "size", nullable = false, length = 10)
	private String size;

	@Column(name = "inStock", nullable = false)
	private boolean inStock = false;

	@Embedded
	private MinimumPriceStock minimumPriceStock;

	public Option(
		UUID modelId,
		String size,
		MinimumPriceStock minimumPriceStock
	) {
		setModelid(modelId);
		setSize(size);
		setMinimumPriceStock(minimumPriceStock);
	}

	public static Option create(String size) {
		Option option = new Option();
		option.setSize(size);
		option.setMinimumPriceStock(null);
		return option;
	}

	public void setOneOption() {
		if (this.size.isBlank()) {
			this.size = "one size";
		}
	}

	public void changeMinimumPriceStock(MinimumPriceStock newMinStock) {
		setMinimumPriceStock(newMinStock);

		if (newMinStock != null) {
			this.inStock = true;
		}
		if (newMinStock == null) {
			this.inStock = false;
		}
	}

	public boolean isEqualId(UUID optionId) {
		return this.optionId.equals(optionId);
	}

	public void checkActive() {
		if (this.isDeleted()) {
			throw AppException.of(ProductErrorCode.INACTIVE_OPTION);
		}
	}

	public void giveModelId(UUID modelId) {
		this.modelId = modelId;
	}

	private void setModelid(UUID modelId) {
		if (modelId == null) {
			throw AppException.of(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 모델ID는 필수 입력값입니다."
			);
		}
		this.modelId = modelId;
	}

	private void setSize(String size) {
		if (size == null || size.isBlank()) {
			size = "one size";
		}
		this.size = size;
	}

	private void setMinimumPriceStock(MinimumPriceStock stock) {
		this.inStock = true;
		if (stock == null) {
			stock = new MinimumPriceStock(null, null, null);
			this.inStock = false;
		}
		this.minimumPriceStock = stock;
	}
}
