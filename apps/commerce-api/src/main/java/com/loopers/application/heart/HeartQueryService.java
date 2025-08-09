package com.loopers.application.heart;

import org.springframework.data.domain.Page;

public interface HeartQueryService {
    Page<Results.HeartResult> getHeartList(CriteriaQuery.GetHeartListCriteria criteria);
}
