package com.limito.limitoresellproduct.domain.vo;

import java.util.UUID;

import com.limito.common.audit.BaseEntity;
import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "p_resell_products")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID productId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "brand_name", nullable = false, length = 100)
	private String brandName;

	@Column(name = "category_id", nullable = false)
	private UUID categoryId;

	public Product(String name, String brandName, UUID categoryId) {
		setName(name);
		setBrandName(brandName);
		setCategoryId(categoryId);
	}

	private void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 상품명은 공백이면 안 됩니다."
			);
		}
		this.name = name;
	}

	private void setBrandName(String brandName) {
		if (brandName == null || brandName.isBlank()) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 브랜드명은 공백이면 안 됩니다."
			);
		}
		this.brandName = brandName;
	}

	private void setCategoryId(UUID categoryId) {
		if (categoryId == null) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 카테고리는 반드시 지정되어야 합니다."
			);
		}
		this.categoryId = categoryId;
	}

	public void validateActive() {
		if (this.isDeleted()) {
			throw new AppException(ProductErrorCode.INACTIVE_PRODUCT);
		}
	}
}
