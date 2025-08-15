package com.loopers.domain.catalog.repository;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.infrastructure.redis.catalog.ProductSliceDto;

public interface ProductCacheRepository {

    ProductRead findById(Long productId);
    ProductSliceDto findSliceByProductListCriteria(ListCondition criteria);

    void save(ListCondition criteria, ProductSliceDto resultRead);

    void save(Long productId, ProductRead productRead);
}
