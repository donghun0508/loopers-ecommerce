package com.loopers.application.catalog;

import com.loopers.application.catalog.Result.ProductDetailResult;
import com.loopers.application.catalog.Result.ProductListResult;
import com.loopers.domain.BaseEntity;
import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandQueryService;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCriteria.ProductDetailCriteria;
import com.loopers.domain.catalog.ProductCriteria.ProductListCriteria;
import com.loopers.domain.catalog.ProductQueryService;
import com.loopers.domain.heart.HeartQueryService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ProductQueryFacade {

    private final ProductQueryService productQueryService;
    private final BrandQueryService brandQueryService;
    private final HeartQueryService heartQueryService;
    private final UserQueryService userQueryService;

    public Page<ProductListResult> getProductList(ProductListCriteria criteria) {
        Page<Product> products = productQueryService.getProductList(criteria);

        List<Long> productIds = products.stream().map(BaseEntity::getId).toList();
        List<Brand> brands = brandQueryService.findAllByProductId(productIds);

        Map<Long, Brand> brandMap = brands.stream()
                .collect(Collectors.toMap(Brand::getId, Function.identity()));
        return null;
    }

    public ProductDetailResult getProductDetail(ProductDetailCriteria criteria) {
        Product product = productQueryService.getProductDetail(criteria.productId());
        Brand brand = brandQueryService.getBrandDetail(product.getBrand().getId());
        User user = userQueryService.getUser(criteria.accountId());

        return null;
    }
}
