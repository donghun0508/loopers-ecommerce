package com.loopers.application.heart;

import com.loopers.application.heart.CriteriaCommand.LikeCriteria;
import com.loopers.application.heart.CriteriaCommand.UnlikeCriteria;
import com.loopers.domain.heart.Heart;
import com.loopers.domain.heart.HeartCreateCommand;
import com.loopers.domain.heart.HeartService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Component
public class HeartFacade {

    private final HeartService heartService;
    private final UserService userService;
    private final HeartProcessor heartProcessor;

    @Transactional
    public void heart(LikeCriteria criteria) {
        heartProcessor.addHeart(criteria.target());
        User user = userService.findByAccountId(criteria.accountId());
        HeartCreateCommand command = HeartCreateCommand.of(user.getId(), criteria.target());
        heartService.create(command);
    }

    @Transactional
    public void unHeart(UnlikeCriteria criteria) {
        heartProcessor.unHeart(criteria.target());
        User user = userService.findByAccountId(criteria.accountId());
        Heart heart = heartService.findByUserIdAndTarget(user.getId(), criteria.target());
        heartService.delete(heart);
    }
}
