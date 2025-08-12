//package com.loopers.temp;
//
//import com.loopers.temp.CriteriaQuery.GetProductDetailCriteria;
//import com.loopers.temp.CriteriaQuery.GetProductListCriteria;
//import com.loopers.temp.Results.GetProductDetailResult;
//import com.loopers.temp.Results.GetProductListResult;
//import com.loopers.support.error.CoreException;
//import com.loopers.support.error.ErrorType;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//class ProductQueryServiceImpl implements ProductQueryService {
//
//    private final ProductQueryRepository productQueryRepository;
//
//    @Override
//    public Page<GetProductListResult> getProductList(GetProductListCriteria criteria) {
//        return productQueryRepository.getProductList(criteria);
//    }
//
//    @Override
//    public GetProductDetailResult getProductDetail(GetProductDetailCriteria criteria) {
//        return productQueryRepository.getProductDetail(criteria)
//                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
//    }
//}
