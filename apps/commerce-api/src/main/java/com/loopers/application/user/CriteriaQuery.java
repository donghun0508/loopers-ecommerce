package com.loopers.application.user;

import com.loopers.domain.user.AccountId;

public class CriteriaQuery {

    public record GetUserCriteria(AccountId accountId) {

        public GetUserCriteria(String accountId) {
            this(AccountId.of(accountId));
        }

        public static GetUserCriteria of(String accountId) {
            return new GetUserCriteria(accountId);
        }
    }

    public record GetUserPointCriteria(AccountId accountId) {

        public GetUserPointCriteria(String accountId) {
            this(AccountId.of(accountId));
        }

        public static GetUserPointCriteria of(String accountId) {
            return new GetUserPointCriteria(accountId);
        }
    }


}
