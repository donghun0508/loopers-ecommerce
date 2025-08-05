package com.loopers.application.order;

import com.loopers.application.order.CriteriaCommand.OrderRequestCriteria;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductManager;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCreateCommand;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @Transactional
    public void requestOrder(OrderRequestCriteria criteria) {
        User buyer = userService.findByAccountId(criteria.accountId());
        List<Product> actualProducts = productService.findAll(criteria.purchaseProductIds());

        ProductManager productManager = ProductManager.assign(actualProducts, criteria.purchaseProducts());
        productManager.decreaseStock();
        buyer.usePoint(productManager.totalPrice());

        OrderCreateCommand orderCreateCommand = OrderCreateCommand.of(buyer.getId(), productManager.snapshots());
        Order order = orderService.create(orderCreateCommand);
    }
}
