package com.loopers.application.catalog;

public interface BrandQueryService {

    Results.GetBrandDetailResult getBrandDetail(CriteriaQuery.GetBrandDetailCriteria criteria);
}
