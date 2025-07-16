package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.application.user.UserPointInfo;
import com.loopers.domain.user.User;
import com.loopers.domain.user.User.Gender;
import com.loopers.domain.user.UserCommand;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;

public class UserV1Dto {

    @Builder
    public record SignUpRequest(
        String userId,
        String email,
        String birth,
        @NotNull GenderRequest gender
    ) {
        public UserCommand.Create toCommand() {
            return UserCommand.Create.builder()
                .userId(userId)
                .email(email)
                .birth(birth)
                .gender(gender.toDomain())
                .build();
        }

        public enum GenderRequest {
            M, F;

            public User.Gender toDomain() {
                return User.Gender.valueOf(this.name());
            }
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record UserResponse(
        Long id,
        String userId,
        String email,
        String birth,
        GenderResponse gender
    ) {
        public static UserResponse from(UserInfo userInfo) {
            Objects.requireNonNull(userInfo, "UserInfo 객체가 null입니다.");

            return UserResponse.builder()
                .id(userInfo.id())
                .userId(userInfo.userId())
                .email(userInfo.email())
                .birth(userInfo.birth())
                .gender(GenderResponse.toResponse(userInfo.gender()))
                .build();
        }

        public enum GenderResponse {
            M, F
            ;

            public static GenderResponse toResponse(Gender gender) {
                return GenderResponse.valueOf(gender.name());
            }
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record UserPointResponse(
        Long point
    ) {

        public static UserPointResponse from(UserPointInfo userPointInfo) {
            Objects.requireNonNull(userPointInfo, "UserPointInfo 객체가 null입니다.");

            return UserPointResponse.builder()
                .point(userPointInfo.point())
                .build();
        }
    }

}
