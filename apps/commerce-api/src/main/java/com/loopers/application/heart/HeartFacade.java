package com.loopers.application.heart;

import com.loopers.application.heart.CriteriaCommand.LikeCriteria;
import com.loopers.application.heart.CriteriaCommand.UnlikeCriteria;
import com.loopers.application.heart.validate.TargetValidateService;
import com.loopers.domain.heart.Heart;
import com.loopers.domain.heart.HeartCreateCommand;
import com.loopers.domain.heart.HeartService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class HeartFacade {

    private final HeartService heartService;
    private final UserService userService;
    private final TargetValidateService targetValidateService;

    public void like(LikeCriteria criteria) {
        User user = userService.findByAccountId(criteria.accountId());
        targetValidateService.validate(criteria.target());
        HeartCreateCommand command = HeartCreateCommand.of(user.getId(), criteria.target());
        Heart heart = heartService.create(command);
    }

    public void unlike(UnlikeCriteria criteria) {
        User user = userService.findByAccountId(criteria.accountId());
        heartService.delete(user.getId(), criteria.target());
    }
}
