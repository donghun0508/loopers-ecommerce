package com.loopers.interfaces.api.catalog;

import com.loopers.application.catalog.ProductQueryFacade;
import com.loopers.application.catalog.CatalogResults.ProductDetailResult;
import com.loopers.application.catalog.CatalogResults.ProductListResult;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.NoCountPageResponse;
import com.loopers.interfaces.api.PaginationRequest;
import com.loopers.interfaces.api.catalog.ProductV1Dto.GetListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller implements ProductV1ApiSpec {

    private final ProductQueryFacade productQueryFacade;

    @Override
    @GetMapping
    public ApiResponse<NoCountPageResponse<GetListResponse>> getProducts(
            @Valid @ModelAttribute ProductV1Dto.GetListRequest request,
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        ListCondition criteria = new ListCondition(request.brandId(), request.sort(), paginationRequest.toPageable());
        Slice<ProductListResult> products = productQueryFacade.getProductList(criteria);
        return ApiResponse.success(NoCountPageResponse.of(products.map(ProductV1Dto.GetListResponse::from)));
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductV1Dto.GetDetailResponse> getProductDetail(
            @PathVariable Long productId,
            @RequestHeader(value = USER_ID, required = false) String userId
    ) {
        DetailCondition criteria = new DetailCondition(productId, userId);
        ProductDetailResult productDetail = productQueryFacade.getProductDetail(criteria);
        return ApiResponse.success(ProductV1Dto.GetDetailResponse.from(productDetail));
    }
}
