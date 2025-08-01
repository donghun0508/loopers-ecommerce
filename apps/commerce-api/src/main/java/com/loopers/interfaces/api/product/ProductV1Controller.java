package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.domain.query.product.ProductQuery.List;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PaginationRequest;
import com.loopers.interfaces.api.product.ProductV1Dto.GetList;
import com.loopers.interfaces.api.product.ProductV1Dto.GetList.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller implements ProductV1ApiSpec {

    private final ProductFacade productFacade;

    @Override
    @GetMapping
    public ApiResponse<Page<Response>> getProducts(
        @ModelAttribute GetList.Request request,
        @ModelAttribute PaginationRequest paginationRequest
    ) {
        Page<List.Response> products = productFacade.getProducts(request.getCondition(), paginationRequest.toPageable());
        return ApiResponse.success(products.map(ProductV1Dto.GetList.Response::from));
    }
}
