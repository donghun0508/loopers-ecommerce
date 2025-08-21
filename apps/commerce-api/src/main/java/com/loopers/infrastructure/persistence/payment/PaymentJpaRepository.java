package com.loopers.infrastructure.persistence.payment;

import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderNumber(String orderNumber);
}
