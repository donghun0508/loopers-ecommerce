package com.loopers.application.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetBrandDetailCriteria;
import com.loopers.application.catalog.Results.GetBrandDetailResult;

public interface BrandQueryFacade {

    GetBrandDetailResult getBrandDetail(GetBrandDetailCriteria criteria);
}
