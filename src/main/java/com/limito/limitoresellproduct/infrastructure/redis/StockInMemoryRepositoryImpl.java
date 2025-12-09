package com.limito.limitoresellproduct.infrastructure.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.limito.limitoresellproduct.domain.repository.StockInMemoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockInMemoryRepositoryImpl implements StockInMemoryRepository {

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public String get(String key) {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		return ops.get(key);
	}

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
}
