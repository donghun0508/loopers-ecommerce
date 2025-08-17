package com.loopers.infrastructure.redis.catalog;

import com.loopers.domain.catalog.ProductCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.redis.domain.AbstractCacheRepository;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
class ProductListRedisCacheRepository extends AbstractCacheRepository<ProductSliceDto, String> {

    public ProductListRedisCacheRepository(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Duration.ofMinutes(10));
    }

    public ProductSliceDto findByListCondition(ListCondition criteria) {
        String cacheKey = generateListKey(criteria);
        return findByKey(cacheKey).orElse(null);
    }

    public void save(ListCondition listCondition, ProductSliceDto result) {
        String cacheKey = generateListKey(listCondition);
        save(cacheKey, result);
    }

    private String generateListKey(ProductCondition.ListCondition criteria) {
        StringBuilder keyBuilder = new StringBuilder("product:list");

        if (criteria.brandId() != null) {
            keyBuilder.append(":brand:").append(criteria.brandId());
        } else {
            keyBuilder.append(":brand:all");
        }

        keyBuilder.append(":sort:").append(criteria.sort().name().toLowerCase());

        keyBuilder.append(":page:").append(criteria.pageable().getPageNumber())
            .append(":size:").append(criteria.pageable().getPageSize());

        return keyBuilder.toString();
    }
}
