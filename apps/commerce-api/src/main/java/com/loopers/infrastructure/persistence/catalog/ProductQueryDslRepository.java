package com.loopers.infrastructure.persistence.catalog;

import static com.loopers.domain.catalog.ProductCondition.ListCondition.SortType.LIKES_DESC;
import static com.loopers.domain.catalog.ProductCondition.ListCondition.SortType.PRICE_ASC;
import static com.loopers.domain.catalog.entity.QBrand.brand;
import static com.loopers.domain.catalog.entity.QProduct.product;
import static java.util.Objects.nonNull;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition.SortType;
import com.loopers.domain.catalog.entity.Product;
import com.loopers.domain.catalog.repository.ProductQueryRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
class ProductQueryDslRepository implements ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findPageByProductListCriteria(ListCondition criteria) {
        // 20ms
        long start1 = System.currentTimeMillis();
        List<Product> content = queryFactory
            .select(product)
            .from(product)
            .where(
                eqBrandId(criteria.brandId())
            )
            .orderBy(getOrderBy(criteria.sort()))
            .limit(criteria.pageable().getPageSize())
            .offset(criteria.pageable().getOffset())
            .fetch();
        long end1 = System.currentTimeMillis();
        log.info("Product content 조회 소요시간: {}ms", (end1 - start1));

        // 10_000_000 // 1초 select count(*) from product;
        JPAQuery<Long> countQuery = queryFactory
            .select(product.count())
            .from(product);

        long start2 = System.currentTimeMillis();
        Page<Product> page = PageableExecutionUtils.getPage(content, criteria.pageable(), countQuery::fetchOne);
        long end2 = System.currentTimeMillis();
        log.info("Product value 조회 소요시간: {}ms", (end2 - start2));

        return page;
    }

    @Override
    public Slice<Product> findSliceByProductListCriteria(ListCondition criteria) {
        List<Product> content = queryFactory
            .select(product)
            .from(product)
            .where(
                eqBrandId(criteria.brandId())
            )
            .orderBy(getOrderBy(criteria.sort()))
            .limit(criteria.pageable().getPageSize() + 1L)
            .offset(criteria.pageable().getOffset())
            .fetch();

        content.forEach(product -> {
            product.getBrand().getName();
        });

        boolean hasNext = content.size() > criteria.pageable().getPageSize();

        List<Product> actualContent = hasNext ?
            content.subList(0, criteria.pageable().getPageSize()) : content;

        return new SliceImpl<>(actualContent, criteria.pageable(), hasNext);
    }

    @Override
    public List<Product> findByProductListCriteria(ListCondition criteria) {
        return queryFactory
            .select(product)
            .from(product)
            .where(
                eqBrandId(criteria.brandId())
            )
            .orderBy(getOrderBy(criteria.sort()))
            .limit(criteria.pageable().getPageSize())
            .offset(criteria.pageable().getOffset())
            .fetch();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .select(product)
                .from(product)
                .join(product.brand, brand).fetchJoin()
                .where(
                    product.id.eq(id)
                )
                .fetchOne()
        );
    }

    @Override
    public Long countByBrandId(Long brandId) {
        return queryFactory
            .select(product.count())
            .from(product)
            .where(
                isActive(),
                eqBrandId(brandId)
            )
            .fetchOne();
    }

    private BooleanExpression isActive() {
        return product.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return nonNull(brandId) ? brand.id.eq(brandId) : null;
    }

    private OrderSpecifier<?>[] getOrderBy(SortType sort) {
        if (sort.equals(LIKES_DESC)) {
            return new OrderSpecifier[]{product.heartCount.value.desc()};
        }
        if (sort.equals(PRICE_ASC)) {
            return new OrderSpecifier[]{product.unitPrice.value.asc()};
        }

        return new OrderSpecifier[]{product.createdAt.desc()};
    }
}
