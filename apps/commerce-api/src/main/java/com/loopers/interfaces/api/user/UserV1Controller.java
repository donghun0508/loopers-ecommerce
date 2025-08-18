package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserResult.UserDetailResult;
import com.loopers.application.user.UserFacade;
import com.loopers.domain.user.AccountId;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @Override
    @PostMapping
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody UserV1Dto.SignUpRequest request) {
        UserDetailResult result = userFacade.signUp(request.criteria());
        return ApiResponse.success(UserResponse.from(result));
    }

    @Override
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUser(@RequestHeader(value = USER_ID) String userId) {
        UserDetailResult userDetailResult = userFacade.getUser(AccountId.of(userId));
        return ApiResponse.success(UserResponse.from(userDetailResult));
    }
}
