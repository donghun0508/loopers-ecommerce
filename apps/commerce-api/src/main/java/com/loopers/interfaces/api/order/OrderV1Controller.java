package com.loopers.interfaces.api.order;

import com.loopers.application.order.CriteriaCommand.PointOrderCriteria;
import com.loopers.application.order.OrderFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderV1Controller implements OrderV1ApiSpec {

    private final OrderFacade orderFacade;

    @Override
    @PostMapping
    public ApiResponse<?> requestOrder(
        @RequestBody OrderV1Dto.OrderRequest request,
        @RequestHeader(value = USER_ID, required = true) String userId
    ) {
        PointOrderCriteria criteria = PointOrderCriteria.of(userId, request.couponId(), request.toPurchaseMap());
        orderFacade.orderByPoint(criteria);
        return ApiResponse.success();
    }
}
