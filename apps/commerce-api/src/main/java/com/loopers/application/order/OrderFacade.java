package com.loopers.application.order;

import com.loopers.application.order.CriteriaCommand.PointOrderCriteria;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductManager;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.catalog.StockRecord;
import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.ApplyCouponCommand;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCreateCommand;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final IssuedCouponService issuedCouponService;

    @Transactional
    public void orderByPoint(PointOrderCriteria criteria) {
        User buyer = userService.findByAccountIdWithLock(criteria.accountId());
        List<Product> actualProducts = productService.findAllWithLock(criteria.purchaseProductIds());

        // 재고 차감
        ProductManager productManager = ProductManager.assign(actualProducts, criteria.purchaseProducts());
        StockRecord stockRecord = productManager.decreaseStock();

        // 주문 생성
        Order order = orderService.create(OrderCreateCommand.of(buyer, stockRecord));

        // 쿠폰 사용
        if (criteria.hasCoupon()) {
            IssuedCoupon coupon = issuedCouponService.findByIdWithLock(criteria.couponId());
            Money paidAmount = coupon.use(buyer.getId(), order.paidAmount());
            order.applyCoupon(ApplyCouponCommand.of(coupon, paidAmount));
        }

        // 결제
        log.debug("결제 전 사용자 포인트 : {}, 주문 금액 : {}, 예상 포인트 : {}, 요청 : {}", buyer.getTotalPoint(), order.paidAmount(), buyer.getTotalPoint().subtract(order.paidAmount()), criteria);
        buyer.payWithPoints(order.paidAmount());
        log.debug("결제 후 사용자 포인트 : {}, 주문 금액 : {}, 요청 : {}", buyer.getTotalPoint(), order.paidAmount(), criteria);
        order.complete();
    }
}
