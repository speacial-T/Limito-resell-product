package com.limito.limitoresellproduct.domain.model;

import java.util.List;
import java.util.UUID;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_resell_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID productId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "brand_name", nullable = false, length = 100)
	private String brandName;

	@Column(name = "category_id", nullable = false)
	private UUID categoryId;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "p_resell_product_options",
		joinColumns = @JoinColumn(name = "product_id")
	)
	private List<Option> options;

	public Product(String name, String brandName, UUID categoryId, List<Option> options) {
		setName(name);
		setBrandName(brandName);
		setCategoryId(categoryId);
		setOptions(options);
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

	private void setOptions(List<Option> options) {
		if (options == null) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 옵션은 적어도 하나 있어야 합니다."
			);
		}
		if (options.size() == 1) {
			options.get(0).setOneOption();
		}
		List<Option> newOptions = Option.validateOptions(options);
		this.options = newOptions;
	}
}
