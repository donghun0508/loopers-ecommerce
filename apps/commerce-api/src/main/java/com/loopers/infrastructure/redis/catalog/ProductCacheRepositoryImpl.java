package com.loopers.infrastructure.redis.catalog;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.repository.ProductCacheRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductCacheRepositoryImpl implements ProductCacheRepository {

    private final ProductListRedisCacheRepository productListRedisCacheRepository;
    private final ProductDetailRedisCacheRepository productDetailRedisCacheRepository;

    @Override
    public ProductRead findById(Long productId) {
        return productDetailRedisCacheRepository.findById(productId);
    }

    @Override
    public void save(Long productId, ProductRead productRead) {
        productDetailRedisCacheRepository.save(productId, productRead);
    }

    @Override
    public ProductSliceDto findSliceByProductListCriteria(ListCondition criteria) {
        return productListRedisCacheRepository.findByListCondition(criteria);
    }

    @Override
    public void save(ListCondition criteria, ProductSliceDto resultRead) {
        productListRedisCacheRepository.save(criteria, resultRead);
    }
}
