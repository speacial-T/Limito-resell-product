package com.limito.limitoresellproduct.domain.model;

import java.util.Optional;
import java.util.UUID;

import com.limito.common.audit.BaseEntity;
import com.limito.common.audit.UserContextHolder;
import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_resell_product_stocks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "option_id", nullable = false)
	private UUID optionId;

	@Column(name = "price", nullable = false)
	@Min(value = 0)
	private int price;

	@Column(name = "sold_out", nullable = false)
	private boolean soldOut = false;

	@Column(name = "seller_id", nullable = false)
	private Long sellerId;

	public Stock(UUID optionId, int price) {
		setOptionId(optionId);
		setPrice(price);
		setSellerId();
	}

	public void changeSoldOutTo(boolean soldOut) {
		this.soldOut = soldOut;
	}

	private void setOptionId(UUID optionId) {
		if (optionId == null) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 옵션ID는 필수 입력값입니다."
			);
		}
		this.optionId = optionId;
	}

	private void setPrice(int price) {
		if (price < 0) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 가격은 0이상 이어야 합니다."
			);
		}
		this.price = price;
	}

	private void setSellerId() {
		Optional<Long> userId = UserContextHolder.getCurrentUserId();
		if (userId.isEmpty()) {
			throw new AppException(
				ProductErrorCode.INVALID_DOMAIN_INFO.getStatus(),
				ProductErrorCode.INVALID_DOMAIN_INFO.getMessage() + ": 판매자를 알 수 없습니다."
			);
		}
		this.sellerId = userId.get();
	}

	public void checkActive() {
		if (this.isDeleted()) {
			throw new AppException(ProductErrorCode.INACTIVE_STOCK);
		}
		if (this.soldOut) {
			throw new AppException(ProductErrorCode.OUT_OF_STOCK);
		}
	}
}
