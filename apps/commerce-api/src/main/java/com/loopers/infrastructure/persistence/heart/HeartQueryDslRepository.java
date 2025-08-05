package com.loopers.infrastructure.persistence.heart;

import com.loopers.application.heart.CriteriaQuery.GetHeartListCriteria;
import com.loopers.application.heart.Results.HeartResult;
import com.loopers.domain.heart.TargetType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.loopers.domain.catalog.QBrand.brand;
import static com.loopers.domain.catalog.QProduct.product;
import static com.loopers.domain.heart.QHeart.heart;
import static com.loopers.domain.user.QUser.user;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
@Repository
class HeartQueryDslRepository implements HeartQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HeartResult> getHeartList(GetHeartListCriteria criteria) {
        List<HeartResult> content = queryFactory
                .select(constructor(HeartResult.class
                        , constructor(HeartResult.ProductInfo.class
                                , product.name
                                , product.stock.count.eq(0L)
                                , product.price.value
                                , brand.id
                                , brand.name
                                , select(heart.count())
                                        .from(heart)
                                        .where(
                                                heart.target.targetId.eq(product.id)
                                                , heart.target.targetType.eq(TargetType.PRODUCT)
                                        )
                        )
                ))
                .from(heart)
                .join(user).on(heart.userId.eq(user.id))
                .join(product).on(heart.target.targetId.eq(product.id).and(heart.target.targetType.eq(TargetType.PRODUCT)))
                .join(brand).on(product.brand.id.eq(brand.id))
                .where(
                        user.accountId.eq(criteria.accountId())
                )
                .orderBy(heart.createdAt.desc())
                .offset(criteria.pageable().getOffset())
                .limit(criteria.pageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(count)
                .from(heart)
                .join(user).on(heart.userId.eq(user.id))
                .where(
                        user.accountId.eq(criteria.accountId())
                );

        return PageableExecutionUtils.getPage(content, criteria.pageable(), countQuery::fetchOne);
    }
}
