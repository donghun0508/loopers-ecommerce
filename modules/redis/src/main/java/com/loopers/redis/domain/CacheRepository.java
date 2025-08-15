package com.loopers.redis.domain;

import java.time.Duration;
import java.util.Optional;

public interface CacheRepository<T, K> {

    Optional<T> findByKey(K key);

    void save(K key, T entity);

    void save(K key, T entity, Duration ttl);

    void delete(K key);

    void deleteAll(String keyPattern);
}
