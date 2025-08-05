package com.loopers.application.heart;

import com.loopers.application.heart.CriteriaQuery.GetHeartListCriteria;
import com.loopers.application.heart.Results.HeartResult;
import org.springframework.data.domain.Page;


public interface HeartQueryFacade {

    Page<HeartResult> getHeartList(GetHeartListCriteria criteria);
}
