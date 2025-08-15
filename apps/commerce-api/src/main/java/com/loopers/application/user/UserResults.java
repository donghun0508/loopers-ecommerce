package com.loopers.application.user;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;

public class UserResults {

    public record UserResult(String accountId, String email, String birth, Gender gender) {
        public static UserResult from(User user) {
            return new UserResult(
                    user.getAccountId().value(),
                    user.getEmail().address(),
                    user.getBirth().day(),
                    user.getGender()
            );
        }
    }

    public record UserPointResult(String accountId, Long point) {

        public static UserPointResult from(User user) {
            return new UserPointResult(user.getAccountId().value(), user.getTotalPoint().value());
        }
    }

}
