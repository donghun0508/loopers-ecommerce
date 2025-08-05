package com.loopers.application.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetProductDetailCriteria;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria;
import com.loopers.application.catalog.Results.GetProductDetailResult;
import com.loopers.application.catalog.Results.GetProductListResult;
import org.springframework.data.domain.Page;

public interface ProductQueryFacade {

    Page<GetProductListResult> getProductList(GetProductListCriteria criteria);

    GetProductDetailResult getProductDetail(GetProductDetailCriteria criteria);
}
