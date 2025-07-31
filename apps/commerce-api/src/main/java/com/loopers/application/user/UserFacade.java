package com.loopers.application.user;

import static java.util.Objects.isNull;

import com.loopers.domain.shared.Money;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.user.UserAlreadyExistsException;
import com.loopers.support.error.user.UserNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    public UserInfo signUp(UserCommand.Create command) {
        validateDuplicateUserId(command.userId());
        User savedUser = userService.create(command);
        return UserInfo.from(savedUser);
    }

    @Transactional
    public UserPointInfo chargePoint(String userId, Long amount) {
        User user = userService.findByUserId(userId);
        user.earnPoints(new Money(amount));
        return UserPointInfo.from(user);
    }

    public UserInfo getUser(String userId) {
        return UserInfo.from(userService.findByUserId(userId));
    }

    public UserPointInfo getUserPoint(String userId) {
        return UserPointInfo.from(userService.findByUserId(userId));
    }

    public void validateDuplicateUserId(String userId) {
        if(userService.existByUserId(userId)) {
            throw new UserAlreadyExistsException();
        }
    }
}
