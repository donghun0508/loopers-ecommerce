package com.loopers.application.order;

import com.loopers.application.order.OrderCommand.PointPaymentOrderCommand;
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
    public void orderByPoint(PointPaymentOrderCommand command) {
        User buyer = userService.findByAccountIdWithLock(command.accountId());
        List<Product> actualProducts = productService.findAllWithLock(command.purchaseProductIds());

        // 재고 차감
        ProductManager productManager = ProductManager.assign(actualProducts, command.purchaseProducts());
        StockRecord stockRecord = productManager.decreaseStock();

        // 주문 생성
        Order order = orderService.create(OrderCreateCommand.of(buyer, stockRecord));

        // 쿠폰 사용
        if (command.hasCoupon()) {
            IssuedCoupon coupon = issuedCouponService.findByIdWithLock(command.couponId());
            Money paidAmount = coupon.use(buyer.getId(), order.paidAmount());
            order.applyCoupon(ApplyCouponCommand.of(coupon, paidAmount));
        }

        // 결제
        log.debug("결제 전 사용자 포인트 : {}, 주문 금액 : {}, 예상 포인트 : {}, 요청 : {}", buyer.getTotalPoint(), order.paidAmount(), buyer.getTotalPoint().subtract(order.paidAmount()), command);
        buyer.payWithPoints(order.paidAmount());
        log.debug("결제 후 사용자 포인트 : {}, 주문 금액 : {}, 요청 : {}", buyer.getTotalPoint(), order.paidAmount(), command);
        order.complete();
    }
}
