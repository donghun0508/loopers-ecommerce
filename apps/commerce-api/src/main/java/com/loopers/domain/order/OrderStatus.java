package com.loopers.domain.order;

public enum OrderStatus {
    PENDING,      // 주문 대기
    PAID,         // 결제 완료
    PREPARING,    // 준비 중
    SHIPPED,      // 배송 중
    DELIVERED,    // 배송 완료
    CANCELLED,    // 취소
    REFUNDED      // 환불
}
