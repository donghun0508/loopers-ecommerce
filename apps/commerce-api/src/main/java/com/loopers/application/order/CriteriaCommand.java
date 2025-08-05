package com.loopers.application.order;

import com.loopers.domain.catalog.Stock;
import com.loopers.domain.user.AccountId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;
import static com.loopers.domain.shared.Preconditions.requireNonEmpty;

public class CriteriaCommand {
    public record OrderRequestCriteria(AccountId accountId, Map<Long, Stock> purchaseProducts) {

        public OrderRequestCriteria(String accountId, Map<Long, Long> productQuantities) {
            this(
                    AccountId.of(requireNonBlank(accountId, "회원 ID는 비어있을 수 없습니다.")),
                    requireNonEmpty(productQuantities, "구매할 상품 목록은 비어있을 수 없습니다.")
                            .entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> Stock.of(entry.getValue())))
            );
            requireNonBlank(accountId, "회원 ID는 비어있을 수 없습니다.");
            requireNonEmpty(productQuantities, "구매할 상품 목록은 비어있을 수 없습니다.");
        }

        public static OrderRequestCriteria of(String accountId, Map<Long, Long> purchaseMap) {
            return new OrderRequestCriteria(accountId, purchaseMap);
        }

        public List<Long> purchaseProductIds() {
            return purchaseProducts.keySet().stream().toList();
        }
    }

}
