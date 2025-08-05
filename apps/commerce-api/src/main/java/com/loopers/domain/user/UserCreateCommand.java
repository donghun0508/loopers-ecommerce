package com.loopers.domain.user;

import lombok.Builder;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

@Builder
public record UserCreateCommand(AccountId accountId, Email email, Birth birth, Gender gender) {

    public UserCreateCommand {
        requireNonNull(accountId, "계정 ID는 비어있을 수 없습니다.");
        requireNonNull(email, "이메일은 비어있을 수 없습니다.");
        requireNonNull(birth, "생년월일은 비어있을 수 없습니다.");
        requireNonNull(gender, "성별은 null일 수 없습니다.");
    }
}
