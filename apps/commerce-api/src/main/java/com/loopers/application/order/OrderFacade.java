package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.StockManager;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.user.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final StockManager stockManager;

    @Transactional
    public void requestOrder(String userId, OrderCommand.Create command) {
        User buyer = userService.findByUserId(userId);
        List<Product> products = productService.findAllById(command.productIds());

        Order order = Order.create(buyer.getId(), command);

        stockManager.deduct(order.lines(), products);

        buyer.usePoints(order.getTotalAmount());

        userService.save(buyer);
        orderService.save(order);
        productService.saveAll(products);
    }
}
