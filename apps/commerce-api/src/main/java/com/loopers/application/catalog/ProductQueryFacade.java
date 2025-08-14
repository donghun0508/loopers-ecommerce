package com.loopers.application.catalog;

import com.loopers.application.catalog.CatalogResults.ProductDetailResult;
import com.loopers.application.catalog.CatalogResults.ProductDetailResult.BrandDetailInfo;
import com.loopers.application.catalog.CatalogResults.ProductDetailResult.ProductDetailInfo;
import com.loopers.application.catalog.CatalogResults.ProductListResult;
import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandQueryService;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductQueryService;
import com.loopers.domain.heart.HeartQueryService;
import com.loopers.domain.heart.Target;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserQueryService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductQueryFacade {

    private final ProductQueryService productQueryService;
    private final BrandQueryService brandQueryService;
    private final UserQueryService userQueryService;
    private final HeartQueryService heartQueryService;

    public Slice<ProductListResult> getProductList(ListCondition condition) {
        Slice<Product> productSlice = productQueryService.getSliceProductList(condition);

        Map<Long, Brand> brandMap = brandQueryService.findAllById(productSlice.getContent().stream()
                .map(p -> p.getBrand().getId())
                .toList())
            .stream()
            .collect(Collectors.toMap(Brand::getId, Function.identity()));

        List<ProductListResult> results = productSlice.getContent().stream()
            .map(product -> ProductListResult.ProductInfo.of(product, brandMap.get(product.getBrand().getId())))
            .toList();

        return new SliceImpl<>(results, productSlice.getPageable(), productSlice.hasNext());
    }

    public ProductDetailResult getProductDetail(DetailCondition criteria) {
        Product product = productQueryService.getProductDetail(criteria.productId());
        Brand brand = brandQueryService.getBrandDetail(product.getBrand().getId());

        boolean isLiked = false;
        if(criteria.accountId() != null) {
            User user = userQueryService.getUser(criteria.accountId());
            isLiked = heartQueryService.existsByUserIdAndTarget(user.getId(), Target.of(product.getId(), TargetType.PRODUCT));
        }

        return ProductDetailResult.builder()
            .productId(product.getId())
            .product(ProductDetailInfo.from(product, isLiked))
            .brand(BrandDetailInfo.from(brand))
            .build();
    }
}
