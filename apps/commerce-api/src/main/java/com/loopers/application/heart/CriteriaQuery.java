package com.loopers.application.heart;

import com.loopers.domain.user.AccountId;
import org.springframework.data.domain.Pageable;

public class CriteriaQuery {
    public record GetHeartListCriteria(AccountId accountId, Pageable pageable) {

        public GetHeartListCriteria(String accountId, Pageable pageable) {
            this(AccountId.of(accountId), pageable);
        }

        public static GetHeartListCriteria of(String accountId, Pageable pageable) {
            return new GetHeartListCriteria(accountId, pageable);
        }
    }
}
