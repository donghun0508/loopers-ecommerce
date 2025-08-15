package com.loopers.domain.catalog.service;

import static java.util.Objects.nonNull;

import com.loopers.aop.execution.ExecutionTime;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductQueryCacheValidate;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.entity.Product;
import com.loopers.domain.catalog.repository.ProductCacheRepository;
import com.loopers.domain.catalog.repository.ProductQueryRepository;
import com.loopers.infrastructure.redis.catalog.ProductSliceDto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;
    private final ProductCacheRepository productCacheRepository;
    private final ProductQueryCacheValidate productQueryCacheValidate;

    @ExecutionTime
    public ProductSliceDto getSliceProductList(ListCondition criteria) {
        if (productQueryCacheValidate.useCache(criteria.getPageNumber())) {
            ProductSliceDto cache = productCacheRepository.findSliceByProductListCriteria(criteria);
            if (nonNull(cache)) {
                log.debug("[Cache hit] for product list criteria: {}", criteria);
                return cache;
            }
            Slice<Product> result = productQueryRepository.findSliceByProductListCriteria(criteria);
            ProductSliceDto productSliceDto = new ProductSliceDto(result);
            productCacheRepository.save(criteria, new ProductSliceDto(result));
            log.debug("[Cache miss] for product list criteria: {}, saving to cache", criteria);
            return productSliceDto;
        } else {
            Slice<Product> sliceByProductListCriteria = productQueryRepository.findSliceByProductListCriteria(criteria);
            return new ProductSliceDto(sliceByProductListCriteria);
        }
    }

    public ProductRead getProductDetail(Long productId) {
        if (productQueryCacheValidate.useCache(productId)) {
            ProductRead cache = productCacheRepository.findById(productId);
            if (nonNull(cache)) {
                log.debug("[Cache hit] for product ID: {}", productId);
                return cache;
            }
            ProductRead productRead = productQueryRepository.findById(productId)
                .map(ProductRead::new)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. ID: " + productId));

            productCacheRepository.save(productId, productRead);
            log.debug("[Cache miss] for product list criteria: {}, saving to cache", productId);
            return productRead;
        } else {
            return productQueryRepository.findById(productId)
                .map(ProductRead::new)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. ID: " + productId));
        }
    }

    public Long getCountByBrandId(Long brandId) {
        return productQueryRepository.countByBrandId(brandId);
    }
}
