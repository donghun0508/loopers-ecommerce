package com.loopers.infrastructure.persistence.catalog;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCriteria;
import com.loopers.domain.catalog.ProductCriteria.ProductListCriteria.SortType;
import com.loopers.domain.catalog.ProductQueryRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.loopers.domain.catalog.ProductCriteria.ProductListCriteria.SortType.PRICE_ASC;
import static com.loopers.domain.catalog.QBrand.brand;
import static com.loopers.domain.catalog.QProduct.product;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Repository
class ProductQueryDslRepository implements ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findByProductListCriteria(ProductCriteria.ProductListCriteria criteria) {
        List<Product> content = queryFactory
                .select(product)
                .from(product)
                .where(
                        isActive()
                        , eqBrandId(criteria.brandId())
                )
                .orderBy(getOrderBy(criteria.sort()))
                .limit(criteria.pageable().getPageSize())
                .offset(criteria.pageable().getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product);

        return PageableExecutionUtils.getPage(content, criteria.pageable(), countQuery::fetchOne);
    }

    private BooleanExpression isActive() {
        return product.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return nonNull(brandId) ? brand.id.eq(brandId) : null;
    }

    private OrderSpecifier<?>[] getOrderBy(SortType sort) {
        if (sort.equals(PRICE_ASC)) {
            return new OrderSpecifier[]{product.unitPrice.value.asc()};
        }

        return new OrderSpecifier[]{product.createdAt.desc()};
    }
}
