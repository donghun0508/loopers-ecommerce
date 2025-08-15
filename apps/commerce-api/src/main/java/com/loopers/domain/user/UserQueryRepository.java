package com.loopers.domain.user;

import java.util.Optional;

public interface UserQueryRepository {

    Optional<User> findByAccountId(AccountId accountId);
}
