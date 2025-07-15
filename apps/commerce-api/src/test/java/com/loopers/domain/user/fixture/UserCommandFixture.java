package com.loopers.domain.user.fixture;

import com.loopers.domain.user.UserCommand;

public class UserCommandFixture {

    public static class Create {
        public static UserCommand.Create.CreateBuilder complete() {
            return UserCommand.Create.builder()
                .userId("hello")
                .email("test@gmail.com")
                .birth("1999-01-01")
                ;
        }
    }
}
