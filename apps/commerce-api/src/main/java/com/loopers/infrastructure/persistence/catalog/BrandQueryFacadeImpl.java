package com.loopers.infrastructure.persistence.catalog;

import com.loopers.application.catalog.BrandQueryFacade;
import com.loopers.application.catalog.CriteriaQuery;
import com.loopers.application.catalog.Results;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class BrandQueryFacadeImpl implements BrandQueryFacade {

    private final BrandQueryRepository brandQueryRepository;

    @Override
    public Results.GetBrandDetailResult getBrandDetail(CriteriaQuery.GetBrandDetailCriteria criteria) {
        return brandQueryRepository.getBrandDetail(criteria)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "브랜드를 찾을 수 없습니다."));
    }
}
