package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.UserCommand;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;

public class UserV1Dto {

    @Builder
    public record SignUpRequest(
        String userId,
        String email,
        String birth
    ) {
        public UserCommand.Create toCommand() {
            return UserCommand.Create.builder()
                .userId(userId)
                .email(email)
                .birth(birth)
                .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record UserResponse(
        Long id,
        String userId,
        String email,
        String birth
    ) {
        public static UserResponse from(UserInfo userInfo) {
            Objects.requireNonNull(userInfo, "UserInfo 객체가 null입니다.");

            return UserResponse.builder()
                .id(userInfo.id())
                .userId(userInfo.userId())
                .email(userInfo.email())
                .birth(userInfo.birth())
                .build();
        }
    }

}
