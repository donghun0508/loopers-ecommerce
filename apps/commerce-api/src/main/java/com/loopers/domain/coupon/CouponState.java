package com.loopers.domain.coupon;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
public record CouponState(@Enumerated(EnumType.STRING) CouponStatus status, LocalDateTime usedAt) {

    public CouponState used() {
        if (this.status != CouponStatus.AVAILABLE) {
            throw new IllegalStateException("CouponState.used().status: 쿠폰은 사용 가능한 상태여야 합니다.");
        }
        return new CouponState(CouponStatus.USED, LocalDateTime.now());
    }

    public CouponState canceled() {
        if (this.status != CouponStatus.AVAILABLE) {
            throw new IllegalStateException("CouponState.canceled().status: 쿠폰은 사용 가능한 상태여야 합니다.");
        }
        return new CouponState(CouponStatus.AVAILABLE, null);
    }

    boolean isUsed() {
        return this.status == CouponStatus.USED;
    }
}
