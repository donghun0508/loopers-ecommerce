package com.loopers.infrastructure.persistence.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetProductDetailCriteria;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria.SortType;
import com.loopers.application.catalog.Results.GetProductDetailResult;
import com.loopers.application.catalog.Results.GetProductListResult;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.user.AccountId;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.loopers.domain.catalog.QBrand.brand;
import static com.loopers.domain.catalog.QProduct.product;
import static com.loopers.domain.heart.QHeart.heart;
import static com.loopers.domain.user.QUser.user;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Expressions.FALSE;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Repository
class ProductQueryDslRepository implements ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetProductListResult> getProductList(GetProductListCriteria criteria) {
        List<GetProductListResult> content = queryFactory
                .select(constructor(GetProductListResult.class
                        , product.id
                        , constructor(GetProductListResult.ProductInfo.class
                                , product.name
                                , product.stock.count.eq(0L)
                                , product.unitPrice.value
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
                        isProductActive()
                        , eqBrandId(criteria.brandId())
                )
                .orderBy(getOrderBy(criteria.sort()))
                .offset(criteria.pageable().getOffset())
                .limit(criteria.pageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(count)
                .from(product);

        return PageableExecutionUtils.getPage(content, criteria.pageable(), countQuery::fetchOne);
    }

    @Override
    public Optional<GetProductDetailResult> getProductDetail(GetProductDetailCriteria criteria) {
        return Optional.ofNullable(
                queryFactory
                        .select(constructor(GetProductDetailResult.class
                                , product.id
                                , constructor(GetProductDetailResult.ProductDetailInfo.class
                                        , product.name
                                        , product.stock.count.eq(0L)
                                        , product.unitPrice.value
                                        , select(heart.count())
                                                .from(heart)
                                                .where(
                                                        heart.target.targetId.eq(product.id)
                                                        , heart.target.targetType.eq(TargetType.PRODUCT)
                                                )
                                        , selectFrom(heart)
                                                .where(
                                                        heart.target.targetId.eq(product.id),
                                                        heart.target.targetType.eq(TargetType.PRODUCT),
                                                        criteria.accountId() != null ?
                                                                heart.userId.eq(
                                                                        select(user.id)
                                                                                .from(user)
                                                                                .where(eqAccountId(criteria.accountId()))
                                                                ) : FALSE
                                                )
                                                .exists()
                                )
                                , constructor(GetProductDetailResult.BrandDetailInfo.class
                                        , brand.id
                                        , brand.name
                                )
                        ))
                        .from(product)
                        .join(brand).on(product.brand.id.eq(brand.id))
                        .where(
                                isProductActive()
                                , eqProductId(criteria.productId())
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isProductActive() {
        return product.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return nonNull(brandId) ? brand.id.eq(brandId) : null;
    }

    private BooleanExpression eqProductId(Long productId) {
        return nonNull(productId) ? product.id.eq(productId) : null;
    }

    private BooleanExpression eqAccountId(AccountId accountId) {
        if (isNull(accountId)) {
            return null;
        }
        return StringUtils.hasText(accountId.value()) ? user.accountId.eq(accountId) : null;
    }

    private OrderSpecifier<?>[] getOrderBy(SortType sortType) {
        return switch (sortType) {
            case LATEST -> new OrderSpecifier[]{product.createdAt.desc()};
            case PRICE_ASC -> new OrderSpecifier[]{product.unitPrice.value.asc()};
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
