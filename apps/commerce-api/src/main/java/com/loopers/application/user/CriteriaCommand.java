package com.loopers.application.user;

import com.loopers.domain.shared.Money;
import com.loopers.domain.user.*;
import lombok.Builder;

public class CriteriaCommand {

    public record UserRegisterCriteria(
            AccountId accountId,
            Email email,
            Birth birth,
            Gender gender
    ) {

        @Builder
        public UserRegisterCriteria(String accountId, String email, String birth, Gender gender) {
            this(
                    AccountId.of(accountId),
                    Email.of(email),
                    Birth.of(birth),
                    gender
            );
        }

        public UserCreateCommand command() {
            return UserCreateCommand.builder()
                    .accountId(accountId)
                    .email(email)
                    .birth(birth)
                    .gender(gender)
                    .build();
        }
    }


    public record UserPointChargeCriteria(AccountId accountId, Money amount) {
        public UserPointChargeCriteria(String accountId, Long amount) {
            this(AccountId.of(accountId), Money.of(amount));
        }

        public static UserPointChargeCriteria of(String accountId, Long amount) {
            return new UserPointChargeCriteria(accountId, amount);
        }
    }
}
