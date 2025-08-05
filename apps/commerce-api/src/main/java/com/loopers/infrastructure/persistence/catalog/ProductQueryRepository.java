package com.loopers.infrastructure.persistence.catalog;

import com.loopers.application.catalog.CriteriaQuery;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria;
import com.loopers.application.catalog.Results.GetProductDetailResult;
import com.loopers.application.catalog.Results.GetProductListResult;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductQueryRepository {
    Page<GetProductListResult> getProductList(GetProductListCriteria criteria);

    Optional<GetProductDetailResult> getProductDetail(CriteriaQuery.GetProductDetailCriteria criteria);
}
