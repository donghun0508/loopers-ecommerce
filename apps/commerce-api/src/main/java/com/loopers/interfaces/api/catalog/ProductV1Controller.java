package com.loopers.interfaces.api.catalog;

import com.loopers.application.catalog.CriteriaQuery.GetProductDetailCriteria;
import com.loopers.application.catalog.CriteriaQuery.GetProductListCriteria;
import com.loopers.application.catalog.ProductQueryFacade;
import com.loopers.application.catalog.Results;
import com.loopers.application.catalog.Results.GetProductDetailResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PaginationRequest;
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
            @ModelAttribute ProductV1Dto.GetListRequest request,
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        GetProductListCriteria criteria = GetProductListCriteria.of(request.brandId(), request.sort(), paginationRequest.toPageable());
        Page<Results.GetProductListResult> products = productQueryFacade.getProductList(criteria);
        return ApiResponse.success(products.map(ProductV1Dto.GetListResponse::from));
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductV1Dto.GetDetailResponse> getProductDetail(
            @PathVariable Long productId,
            @RequestHeader(value = USER_ID, required = false) String userId
    ) {
        GetProductDetailCriteria criteria = GetProductDetailCriteria.of(productId, userId);
        GetProductDetailResult productDetail = productQueryFacade.getProductDetail(criteria);
        return ApiResponse.success(ProductV1Dto.GetDetailResponse.from(productDetail));
    }
}
