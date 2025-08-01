package com.loopers.infrastructure.product;

import static com.loopers.domain.command.heart.QHeart.heart;
import static com.loopers.domain.command.product.QBrand.brand;
import static com.loopers.domain.command.product.QProduct.product;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;
import static java.util.Objects.nonNull;

import com.loopers.domain.command.heart.TargetType;
import com.loopers.domain.query.product.ProductQuery.List.Condition;
import com.loopers.domain.query.product.ProductQuery.List.Response;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class CustomProductQueryRepositoryImpl implements CustomProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Response> findProducts(Pageable pageable) {
        return findProducts(Condition.ofDefault(), pageable);
    }

    @Override
    public Page<Response> findProducts(Condition condition, Pageable pageable) {
        List<Response> content = queryFactory
            .select(Projections.constructor(Response.class
                , product.id
                , Projections.constructor(Response.ProductInfo.class
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
            .from(product)
            .join(brand).on(product.brand.id.eq(brand.id))
            .where(
                isNotDeleted()
                , eqBrandId(condition.brandId())
            )
            .orderBy(getOrderBy(condition.sort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(count)
            .from(product);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isNotDeleted() {
        return product.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return nonNull(brandId) ? product.brand.id.eq(brandId) : null;
    }

    private OrderSpecifier<?>[] getOrderBy(Condition.SortType sortType) {
        return switch (sortType) {
            case LATEST -> new OrderSpecifier[]{product.createdAt.desc()};
            case PRICE_ASC -> new OrderSpecifier[]{product.price.value.asc()};
            case LIKES_DESC -> new OrderSpecifier[]{createLikeCountOrderDesc()};
        };
    }

    private OrderSpecifier<Long> createLikeCountOrderDesc() {
        return new OrderSpecifier<>(
            Order.DESC,
            select(heart.count())
                .from(heart)
                .where(
                    heart.target.targetId.eq(product.id),
                    heart.target.targetType.eq(TargetType.PRODUCT)
                )
        );
    }
}
