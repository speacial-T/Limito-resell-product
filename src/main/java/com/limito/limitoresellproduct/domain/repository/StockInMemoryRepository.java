package com.limito.limitoresellproduct.domain.repository;

public interface StockInMemoryRepository {

	String get(String key);

	void set(String key, String value);
}
