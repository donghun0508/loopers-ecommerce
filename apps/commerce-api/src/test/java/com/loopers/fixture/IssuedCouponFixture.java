package com.loopers.fixture;

import com.loopers.domain.coupon.CouponState;
import com.loopers.domain.coupon.DiscountType;
import com.loopers.domain.coupon.Issuance;
import com.loopers.domain.coupon.IssuedCoupon;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.time.ZonedDateTime;

import static org.instancio.Select.field;

public class IssuedCouponFixture {

    public static Builder builder() {
        return new Builder()
                .expiredAt(ZonedDateTime.now().plusDays(1));
    }

    public static class Builder {

        private final InstancioApi<IssuedCoupon> api;

        public Builder() {
            this.api = Instancio.of(IssuedCoupon.class);
        }

        public Builder discountType(DiscountType discountType) {
            this.api.set(field(IssuedCoupon::getDiscountType), discountType);
            return this;
        }

        public Builder discountValue(Long discountValue) {
            this.api.set(field(IssuedCoupon::getDiscountValue), discountValue);
            return this;
        }

        public Builder couponState(CouponState couponState) {
            this.api.set(field(IssuedCoupon::getCouponState), couponState);
            return this;
        }

        public Builder expiredAt(ZonedDateTime expiredAt) {
            this.api.set(field(Issuance::expiredAt), expiredAt);
            return this;
        }

        public IssuedCoupon build() {
            return api.create();
        }
    }
}
