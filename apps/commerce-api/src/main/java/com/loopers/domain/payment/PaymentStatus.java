package com.loopers.domain.payment;

public enum PaymentStatus {
    PENDING,    // 결제 진행 중
    REQUESTED, // 결제 요청
    COMPLETED,  // 결제 완료
    FAILED,     // 결제 실패
}
