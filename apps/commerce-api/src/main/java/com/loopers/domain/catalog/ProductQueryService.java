package com.loopers.domain.catalog;

import com.loopers.aop.execution.ExecutionTime;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public Page<Product> getPageProductList(ListCondition criteria) {
        return productQueryRepository.findPageByProductListCriteria(criteria);
    }

    @ExecutionTime
    public Slice<Product> getSliceProductList(ListCondition criteria) {
        return productQueryRepository.findSliceByProductListCriteria(criteria);
    }

    public List<Product> getProductList(ListCondition criteria) {
        return productQueryRepository.findByProductListCriteria(criteria);
    }

    public Long getCountByBrandId(Long brandId) {
        return productQueryRepository.countByBrandId(brandId);
    }

    public Product getProductDetail(Long productId) {
        return productQueryRepository.findById(productId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. ID: " + productId));
    }
}
