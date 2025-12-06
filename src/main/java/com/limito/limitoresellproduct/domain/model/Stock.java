package com.limito.limitoresellproduct.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_resell_product_stocks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID stockId;

	@Column(name = "option_id", nullable = false)
	private UUID optionId;

	@Column(name = "price", nullable = false)
	@Size(min = 0)
	private int price;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(name = "seller_id", nullable = false)
	private UUID sellerId;
}
