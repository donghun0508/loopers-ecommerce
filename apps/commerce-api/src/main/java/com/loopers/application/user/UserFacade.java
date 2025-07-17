package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserCommand.Create;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.user.UserAlreadyExistsException;
import com.loopers.support.error.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    public UserInfo signUp(UserCommand.Create command) {
        validateDuplicateUserId(command);
        User savedUser = userService.create(command);
        return UserInfo.from(savedUser);
    }

    @Transactional
    public UserPointInfo chargePoint(String userId, Long amount) {
        User user = userService.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        user.chargePoint(amount);
        return UserPointInfo.from(user);
    }

    public UserInfo getUser(String userId) {
        return userService.findByUserId(userId)
            .map(UserInfo::from)
            .orElseThrow(UserNotFoundException::new);
    }

    public UserPointInfo getUserPoint(String userId) {
        return userService.findByUserId(userId)
            .map(UserPointInfo::from)
            .orElseThrow(UserNotFoundException::new);
    }

    private void validateDuplicateUserId(Create command) {
        userService.findByUserId(command.userId())
            .ifPresent(user -> {
                throw new UserAlreadyExistsException();
            });
    }
}
