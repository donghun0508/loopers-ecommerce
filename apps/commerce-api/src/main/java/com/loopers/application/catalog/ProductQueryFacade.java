package com.loopers.application.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetProductDetailCriteria;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria;
import com.loopers.application.catalog.Results.GetProductDetailResult;
import com.loopers.application.catalog.Results.GetProductListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductQueryFacade {

    private final ProductQueryService productQueryService;

    public Page<GetProductListResult> getProductList(GetProductListCriteria criteria) {
        return productQueryService.getProductList(criteria);
    }

    public GetProductDetailResult getProductDetail(GetProductDetailCriteria criteria) {
        return productQueryService.getProductDetail(criteria);
    }
}
