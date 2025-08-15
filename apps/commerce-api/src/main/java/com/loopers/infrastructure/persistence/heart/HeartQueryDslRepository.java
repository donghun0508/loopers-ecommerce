package com.loopers.infrastructure.persistence.heart;

import static com.loopers.domain.heart.QHeart.heart;

import com.loopers.domain.heart.HeartQueryRepository;
import com.loopers.domain.heart.Target;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class HeartQueryDslRepository implements HeartQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserIdAndTarget(Long userId, Target target) {
        Integer result = queryFactory
            .selectOne()
            .from(heart)
            .where(
                heart.userId.eq(userId)
                , heart.target.eq(target)
            )
            .fetchFirst();

        return result != null;
    }
}
