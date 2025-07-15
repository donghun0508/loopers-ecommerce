package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserCommand.Create;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.user.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    public UserInfo signUp(UserCommand.Create command) {
        validateDuplicateUserId(command);
        User savedUser = userService.create(command);
        return UserInfo.from(savedUser);
    }

    private void validateDuplicateUserId(Create command) {
        userService.findByUserId(command.userId())
            .ifPresent(user -> {
                throw new UserAlreadyExistsException();
            });
    }
}
