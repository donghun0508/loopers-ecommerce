package com.loopers.application.payment;

import com.loopers.application.payment.data.PaymentMethod;
import com.loopers.domain.order.OrderNumber;

public class PaymentCommand {

    public record PaymentRequestCommand(OrderNumber orderNumber, PaymentMethod paymentMethod) {

    }

}
