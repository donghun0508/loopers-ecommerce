package com.loopers.interfaces.api.catalog;

import com.loopers.application.catalog.ProductQueryFacade;
import com.loopers.application.catalog.Result;
import com.loopers.application.catalog.Result.ProductDetailResult;
import com.loopers.application.catalog.Result.ProductListResult;
import com.loopers.domain.catalog.ProductCriteria.ProductDetailCriteria;
import com.loopers.domain.catalog.ProductCriteria.ProductListCriteria;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PaginationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller implements ProductV1ApiSpec {

    private final ProductQueryFacade productQueryFacade;

    @Override
    @GetMapping
    public ApiResponse<Page<ProductV1Dto.GetListResponse>> getProducts(
            @Valid @ModelAttribute ProductV1Dto.GetListRequest request,
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        ProductListCriteria criteria = new ProductListCriteria(request.brandId(), request.sort(), paginationRequest.toPageable());
        Page<ProductListResult> products = productQueryFacade.getProductList(criteria);
        return ApiResponse.success(products.map(ProductV1Dto.GetListResponse::from));
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductV1Dto.GetDetailResponse> getProductDetail(
            @PathVariable Long productId,
            @RequestHeader(value = USER_ID, required = false) String userId
    ) {
        ProductDetailCriteria criteria = new ProductDetailCriteria(productId, userId);
        ProductDetailResult productDetail = productQueryFacade.getProductDetail(criteria);
        return ApiResponse.success(ProductV1Dto.GetDetailResponse.from(productDetail));
    }
}
