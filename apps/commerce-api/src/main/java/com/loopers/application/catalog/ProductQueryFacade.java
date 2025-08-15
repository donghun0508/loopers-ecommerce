package com.loopers.application.catalog;

import com.loopers.application.catalog.CatalogResults.ProductDetailResult;
import com.loopers.application.catalog.CatalogResults.ProductDetailResult.BrandDetailInfo;
import com.loopers.application.catalog.CatalogResults.ProductDetailResult.ProductDetailInfo;
import com.loopers.application.catalog.CatalogResults.ProductListResult;
import com.loopers.application.catalog.CatalogResults.ProductListResult.ProductInfo;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.entity.Product;
import com.loopers.domain.catalog.service.ProductQueryService;
import com.loopers.domain.heart.HeartQueryService;
import com.loopers.domain.heart.Target;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserQueryService;
import com.loopers.infrastructure.redis.catalog.ProductSliceDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductQueryFacade {

    private final ProductQueryService productQueryService;
    private final UserQueryService userQueryService;
    private final HeartQueryService heartQueryService;

    public Slice<ProductListResult> getProductList(ListCondition condition) {
        ProductSliceDto productSlice = productQueryService.getSliceProductList(condition);
        List<ProductListResult> results = productSlice.getContent().stream().map(ProductInfo::of).toList();
        return new SliceImpl<>(results, PageRequest.of(productSlice.getPageNumber(), productSlice.getPageSize()), productSlice.isHasNext());
    }

    public ProductDetailResult getProductDetail(DetailCondition criteria) {
        ProductRead productRead = productQueryService.getProductDetail(criteria.productId());

        boolean isLiked = false;
        if (criteria.accountId() != null) {
            User user = userQueryService.getUser(criteria.accountId());
            isLiked = heartQueryService.existsByUserIdAndTarget(user.getId(), Target.of(productRead.getId(), TargetType.PRODUCT));
        }

        return ProductDetailResult.builder()
            .productId(productRead.getId())
            .product(ProductDetailInfo.from(productRead, isLiked))
            .brand(BrandDetailInfo.from(productRead))
            .build();
    }
}
