package com.loopers.application.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetBrandDetailCriteria;
import com.loopers.application.catalog.Results.GetBrandDetailResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandQueryFacade {

    private final BrandQueryService brandQueryService;

    public GetBrandDetailResult getBrandDetail(GetBrandDetailCriteria criteria) {
        return brandQueryService.getBrandDetail(criteria);
    }
}
