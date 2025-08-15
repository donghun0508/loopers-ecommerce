package com.loopers.infrastructure.persistence.user;

import static com.loopers.domain.user.QUser.user;

import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.QUser;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class UserQueryDslRepository implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByAccountId(AccountId accountId) {
        return Optional.ofNullable(queryFactory
            .select(user)
            .from(user)
            .where(
                user.accountId.eq(accountId)
            )
            .fetchOne());
    }
}
