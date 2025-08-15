package com.loopers.application.order;

import com.loopers.domain.catalog.vo.Stock;
import com.loopers.domain.user.AccountId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;
import static com.loopers.domain.shared.Preconditions.requireNonEmpty;
import static java.util.Objects.nonNull;

public class CriteriaCommand {
    public record PointOrderCriteria(AccountId accountId, Long couponId, Map<Long, Stock> purchaseProducts) {

        public PointOrderCriteria(String accountId, Long couponId, Map<Long, Long> productQuantities) {
            this(
                    AccountId.of(requireNonBlank(accountId, "회원 ID는 비어있을 수 없습니다.")),
                    couponId,
                    requireNonEmpty(productQuantities, "구매할 상품 목록은 비어있을 수 없습니다.")
                            .entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> Stock.of(entry.getValue())))
            );
            requireNonBlank(accountId, "회원 ID는 비어있을 수 없습니다.");
            requireNonEmpty(productQuantities, "구매할 상품 목록은 비어있을 수 없습니다.");
        }

        public static PointOrderCriteria of(String accountId, Long countId, Map<Long, Long> purchaseMap) {
            return new PointOrderCriteria(accountId, countId, purchaseMap);
        }

        public boolean hasCoupon() {
            return nonNull(couponId);
        }

        public List<Long> purchaseProductIds() {
            return purchaseProducts.keySet().stream().toList();
        }
    }

}
