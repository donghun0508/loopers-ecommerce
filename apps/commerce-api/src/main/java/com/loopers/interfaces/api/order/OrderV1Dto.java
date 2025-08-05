package com.loopers.interfaces.api.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderV1Dto {
    public record OrderRequest(List<PurchaseItem> items) {

        public Map<Long, Long> toPurchaseMap() {
            return items.stream()
                    .collect(Collectors.toMap(PurchaseItem::productId, PurchaseItem::quantity));
        }

        public record PurchaseItem(Long productId, Long quantity) {

        }
    }
}
