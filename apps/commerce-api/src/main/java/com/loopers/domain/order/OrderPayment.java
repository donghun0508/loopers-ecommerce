package com.loopers.domain.order;

import com.loopers.domain.shared.Money;
import jakarta.persistence.Embeddable;

@Embeddable
public record OrderPayment(Long issuedCouponId, Money totalPrice, Money paidPrice) {

    public static OrderPayment before(Money totalPrice) {
        return new OrderPayment(null, totalPrice, totalPrice);
    }

    public static OrderPayment after(Long issuedCouponId, Money totalAmount, Money paidAmount) {
        return new OrderPayment(issuedCouponId, totalAmount, paidAmount);
    }
}
