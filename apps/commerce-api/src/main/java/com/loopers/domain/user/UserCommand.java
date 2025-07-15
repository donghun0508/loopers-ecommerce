package com.loopers.domain.user;

import lombok.Builder;

public class UserCommand {

    @Builder
    public record Create(String userId, String email, String birth) {

    }

}
