package com.loopers.application.heart;


import com.loopers.domain.heart.Heart;
import com.loopers.domain.heart.HeartQueryService;
import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HeartQueryFacade {

    private final UserQueryService userQueryService;
    private final HeartQueryService heartQueryService;

    public Page<Results.HeartResult> getHeartList(String userId, Pageable pageable) {
        User user = userQueryService.getUser(AccountId.of(userId));

        List<Heart> hearts = heartQueryService.getHeartList(user.getId(), pageable);
        return null;
    }
}
