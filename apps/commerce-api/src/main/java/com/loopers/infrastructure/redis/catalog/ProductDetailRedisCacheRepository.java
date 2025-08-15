package com.loopers.infrastructure.redis.catalog;

import com.loopers.domain.catalog.ProductRead;
import com.loopers.redis.domain.AbstractCacheRepository;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
class ProductDetailRedisCacheRepository extends AbstractCacheRepository<ProductRead, String> {

    public ProductDetailRedisCacheRepository(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Duration.ofMinutes(10));
    }

    public ProductRead findById(Long productId) {
        String cacheKey = generateKey(productId);
        return findByKey(cacheKey).orElse(null);
    }

    public void save(Long productId, ProductRead productRead) {
        String cacheKey = generateKey(productId);
        save(cacheKey, productRead);
    }

    private String generateKey(Long productId) {
        return "product:detail:" + productId;
    }
}
