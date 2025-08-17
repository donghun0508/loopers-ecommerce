package com.loopers.interfaces.api.user;

import com.loopers.application.user.CriteriaQuery.GetUserCriteria;
import com.loopers.application.user.UserResults.UserResult;
import com.loopers.application.user.UserFacade;
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
        UserResult result = userFacade.signUp(request.criteria());
        return ApiResponse.success(UserResponse.from(result));
    }

    @Override
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUser(@RequestHeader(value = USER_ID) String userId) {
        GetUserCriteria criteria = GetUserCriteria.of(userId);
        UserResult userResult = userFacade.getUser(criteria);
        return ApiResponse.success(UserResponse.from(userResult));
    }
}
