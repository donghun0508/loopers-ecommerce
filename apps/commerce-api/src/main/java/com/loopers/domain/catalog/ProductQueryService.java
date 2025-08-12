package com.loopers.domain.catalog;

import com.loopers.domain.catalog.ProductCriteria.ProductListCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public Page<Product> getProductList(ProductListCriteria criteria) {
        return productQueryRepository.findByProductListCriteria(criteria);
    }

    public Long getCountByBrandId(Long brandId) {
        return null;
    }

    public Product getProductDetail(Long productId) {
        return null;
    }
}
