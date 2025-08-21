package com.loopers.application.payment;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.handler.PaymentHandler;
import com.loopers.application.payment.handler.PaymentHandlers;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PaymentFacade {

    private final PaymentService paymentService;
    private final PaymentHandlers paymentHandlers;

    @Transactional
    public void paymentRequest(PaymentRequestCommand command) {
        PaymentHandler paymentHandler = paymentHandlers.getPaymentHandler(command.paymentMethod());
        Payment payment = paymentHandler.process(command);
        paymentService.create(payment);
    }
}
