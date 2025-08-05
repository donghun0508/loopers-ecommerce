package com.loopers.infrastructure.persistence.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetBrandDetailCriteria;
import com.loopers.application.catalog.Results;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.loopers.domain.catalog.QBrand.brand;
import static com.loopers.domain.catalog.QProduct.product;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
@Repository
class BrandQueryDslRepository implements BrandQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Results.GetBrandDetailResult> getBrandDetail(GetBrandDetailCriteria criteria) {
        return Optional.ofNullable(
                queryFactory
                        .select(constructor(Results.GetBrandDetailResult.class
                                        , brand.id
                                        , brand.name
                                        , select(product.count())
                                                .from(product)
                                                .where(
                                                        product.brand.id.eq(brand.id)
                                                )
                                )
                        )
                        .from(brand)
                        .where(
                                isBrandActive(),
                                eqBrandId(criteria.brandId())
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isBrandActive() {
        return brand.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return brand.id.eq(brandId);
    }

}
