//package com.loopers.temp;
//
//import com.loopers.temp.CriteriaQuery.GetProductDetailCriteria;
//import com.loopers.temp.CriteriaQuery.GetProductListCriteria;
//import com.loopers.temp.Results.GetProductDetailResult;
//import com.loopers.temp.Results.GetProductListResult;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class ProductQueryFacadeTemp {
//
//    private final ProductQueryService productQueryService;
//
//    public Page<GetProductListResult> getProductList(GetProductListCriteria criteria) {
//        return productQueryService.getProductList(criteria);
//    }
//
//    public GetProductDetailResult getProductDetail(GetProductDetailCriteria criteria) {
//        return productQueryService.getProductDetail(criteria);
//    }
//}
