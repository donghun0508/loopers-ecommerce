package com.loopers.application.heart;

import com.loopers.domain.heart.Target;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.user.AccountId;

public class CriteriaCommand {
    public record LikeCriteria(AccountId accountId, Target target) {

        public LikeCriteria(String accountId, Long targetId, TargetType targetType) {
            this(AccountId.of(accountId), Target.of(targetId, targetType));
        }

        public static LikeCriteria of(String accountId, Long targetId, TargetType targetType) {
            return new LikeCriteria(accountId, targetId, targetType);
        }
    }

    public record UnlikeCriteria(AccountId accountId, Target target) {

        public UnlikeCriteria(String accountId, Long targetId, TargetType targetType) {
            this(AccountId.of(accountId), Target.of(targetId, targetType));
        }

        public static UnlikeCriteria of(String accountId, Long productId, TargetType targetType) {
            return new UnlikeCriteria(accountId, productId, targetType);
        }
    }

}
