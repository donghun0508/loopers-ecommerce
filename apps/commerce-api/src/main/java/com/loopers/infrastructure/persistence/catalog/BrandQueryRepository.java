package com.loopers.infrastructure.persistence.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetBrandDetailCriteria;
import com.loopers.application.catalog.Results.GetBrandDetailResult;

import java.util.Optional;

interface BrandQueryRepository {
    Optional<GetBrandDetailResult> getBrandDetail(GetBrandDetailCriteria criteria);
}
