package com.loopers.infrastructure.persistence.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetProductDetailCriteria;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria;
import com.loopers.application.catalog.ProductQueryFacade;
import com.loopers.application.catalog.Results.GetProductDetailResult;
import com.loopers.application.catalog.Results.GetProductListResult;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductQueryFacadeImpl implements ProductQueryFacade {

    private final ProductQueryRepository productQueryRepository;

    @Override
    public Page<GetProductListResult> getProductList(GetProductListCriteria criteria) {
        return productQueryRepository.getProductList(criteria);
    }

    @Override
    public GetProductDetailResult getProductDetail(GetProductDetailCriteria criteria) {
        return productQueryRepository.getProductDetail(criteria)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
    }
}
