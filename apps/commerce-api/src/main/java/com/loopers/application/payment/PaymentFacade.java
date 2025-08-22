package com.loopers.application.payment;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.PaymentCommand.PaymentSyncCommand;
import com.loopers.application.payment.handler.PaymentHandler;
import com.loopers.application.payment.handler.PaymentHandlers;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.CardPayment;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PaymentStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PaymentFacade {

    private final PaymentService paymentService;
    private final PaymentHandlers paymentHandlers;
    private final OrderService orderService;

    @Transactional
    public void paymentRequest(PaymentRequestCommand command) {
        PaymentHandler paymentHandler = paymentHandlers.getPaymentHandler(command.paymentMethod());
        Payment payment = paymentHandler.process(command);
        paymentService.create(payment);
    }

    @Transactional
    public void syncPayment(PaymentSyncCommand command) {
        if (command.isSuccess()) {
            Payment payment = paymentService.findByOrderNumber(command.orderNumber());
            Order order = orderService.findByOrderNumber(command.orderNumber());

            payment.complete();
            order.complete();
        }
    }

    public List<String> getSyncTransactionIds() {
        return paymentService.findAllByStatus(PaymentStatus.REQUESTED)
            .stream()
            .filter(CardPayment.class::isInstance)
            .map(CardPayment.class::cast)
            .map(CardPayment::getTransactionId)
            .collect(Collectors.toList());
    }
}
