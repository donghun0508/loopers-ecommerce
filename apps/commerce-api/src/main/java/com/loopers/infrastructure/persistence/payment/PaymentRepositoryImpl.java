package com.loopers.infrastructure.persistence.payment;

import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByOrderNumber(OrderNumber orderNumber) {
        return paymentJpaRepository.findByOrderNumber(orderNumber.number());
    }
}
