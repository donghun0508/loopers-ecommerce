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

    @Transactional
    public UserInfo signUp(UserCommand.Create command) {
        validateDuplicateUserId(command);
        User savedUser = userService.create(command);
        return UserInfo.from(savedUser);
    }

    public UserInfo getUser(Long id) {
        return userService.findById(id)
            .map(UserInfo::from)
            .orElseThrow(UserNotFoundException::new);
    }

    private void validateDuplicateUserId(Create command) {
        userService.findByUserId(command.userId())
            .ifPresent(user -> {
                throw new UserAlreadyExistsException();
            });
    }
}
