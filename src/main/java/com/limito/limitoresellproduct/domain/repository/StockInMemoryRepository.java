package com.limito.limitoresellproduct.domain.repository;

public interface StockInMemoryRepository {

	void set(String key, String value);

	void delete(String key);

	String getOrElseThrow(String key);

	String get(String key);
}
