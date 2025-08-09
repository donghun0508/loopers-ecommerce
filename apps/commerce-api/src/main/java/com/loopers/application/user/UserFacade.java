package com.loopers.application.user;

import com.loopers.application.user.CriteriaCommand.UserPointChargeCriteria;
import com.loopers.application.user.CriteriaCommand.UserRegisterCriteria;
import com.loopers.application.user.CriteriaQuery.GetUserCriteria;
import com.loopers.application.user.CriteriaQuery.GetUserPointCriteria;
import com.loopers.application.user.Results.UserPointResult;
import com.loopers.application.user.Results.UserResult;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCreateCommand;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    public UserResult signUp(UserRegisterCriteria criteria) {
        UserCreateCommand command = criteria.command();
        User user = userService.create(command);
        return UserResult.from(user);
    }

    @Transactional
    public UserPointResult chargePoint(UserPointChargeCriteria criteria) {
        User user = userService.findByAccountId(criteria.accountId());
        user.chargePoint(criteria.amount());
        return UserPointResult.from(user);
    }

    public UserResult getUser(GetUserCriteria criteria) {
        User user = userService.findByAccountId(criteria.accountId());
        return UserResult.from(user);
    }

    public UserPointResult getPoint(GetUserPointCriteria criteria) {
        User user = userService.findByAccountId(criteria.accountId());
        return UserPointResult.from(user);
    }
}
