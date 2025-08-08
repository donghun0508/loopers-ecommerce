package com.loopers.domain.order;

import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.shared.Money;

public record ApplyCouponCommand(Long couponId, Money paidAmount) {

    public static ApplyCouponCommand of(IssuedCoupon coupon, Money paidAmount) {
        return new ApplyCouponCommand(coupon.getId(), paidAmount);
    }
}
