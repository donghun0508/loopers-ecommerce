package com.loopers.infrastructure.persistence.catalog;

import static com.loopers.domain.catalog.QBrand.brand;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class BrandQueryDslRepository implements BrandQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Brand> findAllById(List<Long> brandIds) {
        return queryFactory
            .select(brand)
            .from(brand)
            .where(
                brand.id.in(brandIds)
            )
            .fetch();
    }

    @Override
    public Optional<Brand> findById(Long brandId) {
        return Optional.ofNullable(queryFactory
            .select(brand)
            .from(brand)
            .where(
                brand.id.eq(brandId)
            )
            .fetchOne());
    }
}
