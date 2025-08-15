package com.loopers.domain.shared;

import java.time.Duration;

public interface CacheRepository<T, K> {

    T findByKey(K key);

    void save(K key, T value);

    void save(K key, T value, Duration ttl);
}
