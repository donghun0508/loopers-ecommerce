package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.UserCommand;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.SignUpRequest;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        UserCommand.Create command = request.toCommand();
        UserInfo userInfo = userFacade.signUp(command);
        return ApiResponse.success(UserResponse.from(userInfo));
    }

    @GetMapping("/{id}")
    @Override
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        UserInfo userInfo = userFacade.getUser(id);
        return ApiResponse.success(UserResponse.from(userInfo));
    }
}
