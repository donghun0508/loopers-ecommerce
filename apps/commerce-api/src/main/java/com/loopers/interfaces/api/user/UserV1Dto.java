package com.loopers.interfaces.api.user;

import com.loopers.application.user.CriteriaCommand.UserRegisterCriteria;
import com.loopers.application.user.Results.UserResult;
import com.loopers.domain.user.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;

import static java.util.Objects.requireNonNull;

public class UserV1Dto {
    @Builder
    public record SignUpRequest(
            String userId,
            String email,
            String birth,
            @NotNull GenderRequest gender
    ) {
        public UserRegisterCriteria criteria() {
            return UserRegisterCriteria.builder()
                    .accountId(userId)
                    .email(email)
                    .birth(birth)
                    .build();
        }

        public enum GenderRequest {
            M, F;

            public Gender toDomain() {
                return Gender.valueOf(this.name());
            }
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record UserResponse(
            String userId,
            String email,
            String birth,
            GenderResponse gender
    ) {
        public static UserResponse from(UserResult userResult) {
            requireNonNull(userResult, "MemberResponse 객체가 null입니다.");

            return UserResponse.builder()
                    .userId(userResult.accountId())
                    .email(userResult.email())
                    .birth(userResult.birth())
                    .gender(GenderResponse.toResponse(userResult.gender()))
                    .build();
        }

        public enum GenderResponse {
            M, F;

            public static GenderResponse toResponse(Gender gender) {
                return GenderResponse.valueOf(gender.name());
            }
        }
    }
}
