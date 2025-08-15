package com.loopers.redis.domain;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public abstract class AbstractCacheRepository<T, K> implements CacheRepository<T, K> {

    protected final RedisTemplate<String, Object> redisTemplate;
    protected final Duration defaultTtl;

    protected AbstractCacheRepository(RedisTemplate<String, Object> redisTemplate, Duration defaultTtl) {
        this.redisTemplate = redisTemplate;
        this.defaultTtl = defaultTtl;
    }

    @Override
    public Optional<T> findByKey(K key) {
        try {
            @SuppressWarnings("unchecked")
            T cached = (T) redisTemplate.opsForValue().get(key.toString());
            return Optional.ofNullable(cached);
        } catch (Exception e) {
            log.warn("Cache get failed for key: {}", key, e);
            return Optional.empty();
        }
    }

    @Override
    public void save(K key, T entity) {
        save(key, entity, defaultTtl);
    }

    @Override
    public void save(K key, T entity, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key.toString(), entity, ttl);
        } catch (Exception e) {
            log.error("Cache save failed for key: {}", key, e);
        }
    }

    @Override
    public void delete(K key) {
        try {
            redisTemplate.delete(key.toString());
        } catch (Exception e) {
            log.error("Cache delete failed for key: {}", key, e);
        }
    }

    @Override
    public void deleteAll(String keyPattern) {
        try {
            Set<String> keys = redisTemplate.keys(keyPattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.error("Cache deleteAll failed for pattern: {}", keyPattern, e);
        }
    }
}
