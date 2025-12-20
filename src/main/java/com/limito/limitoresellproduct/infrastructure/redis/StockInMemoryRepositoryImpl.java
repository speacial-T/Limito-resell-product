package com.limito.limitoresellproduct.infrastructure.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.repository.StockInMemoryRepository;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockInMemoryRepositoryImpl implements StockInMemoryRepository {

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public void set(String key, String value) {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set(key, value);
	}

	@Override
	public void delete(String key) {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.getAndDelete(key);
	}

	@Override
	public String getOrElseThrow(String key) {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		String result = ops.get(key);
		if (result == null) {
			throw AppException.of(ProductErrorCode.OUT_OF_STOCK);
		}
		return result;
	}
}
