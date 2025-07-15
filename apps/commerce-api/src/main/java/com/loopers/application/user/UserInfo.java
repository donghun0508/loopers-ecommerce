package com.loopers.application.user;

import com.loopers.domain.user.User;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserInfo(
    Long id,
    String userId,
    String email,
    String birth
) {

    public static UserInfo from(User user) {
        Objects.requireNonNull(user, "User 객체가 null입니다.");

        return UserInfo.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .email(user.getEmail())
            .birth(user.getBirth())
            .build();
    }
}
