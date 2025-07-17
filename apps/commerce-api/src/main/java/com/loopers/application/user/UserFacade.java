package com.loopers.application.user;

import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
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
    private final PointService pointService;

    @Transactional
    public UserInfo signUp(UserCommand.Create command) {
        validateDuplicateUserId(command);
        User savedUser = userService.create(command);
        initializePoint(savedUser);
        return UserInfo.from(savedUser);
    }

    public UserInfo getUser(String userId) {
        return userService.findByUserId(userId)
            .map(UserInfo::from)
            .orElseThrow(UserNotFoundException::new);
    }

    private void validateDuplicateUserId(Create command) {
        userService.findByUserId(command.userId())
            .ifPresent(user -> {
                throw new UserAlreadyExistsException();
            });
    }

    private void initializePoint(User user) {
        PointCommand.Create command = PointCommand.Create.of(user.getId());
        pointService.create(command);
    }
}
