package com.loopers.domain.catalog;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface ProductQueryRepository {

    Page<Product> findPageByProductListCriteria(ListCondition criteria);

    Optional<Product> findById(Long id);

    Long countByBrandId(Long brandId);

    List<Product> findByProductListCriteria(ListCondition criteria);

    Slice<Product> findSliceByProductListCriteria(ListCondition criteria);
}
