```mermaid
---
title: 29CART 도메인 모델
---
classDiagram
    direction LR

    class Member {
        -Point point
        +usePoints(Money amount) void
        +earnPoints(Money amount) void
        +getAvailablePoints() Money
    }

    class Point {
        -Money balance
        +deduct(Money amount) void
        +credit(Money amount) void
        +hasEnoughBalance(Money amount) boolean
    }

    class Brand {
        -Instruction introduction
        -List~Product~ products
    }

    class Heart {
        -Member member
        -Product product
        +create(Member member, Product product) Heart
    }

    class Product {
        -Quantity quantity
        -Money price
        -Instruction instruction
        +reduceInventory(Quantity quantity) void
        +hasEnoughStock(Quantity quantity) boolean
        +getPrice() Money
    }

    class Order {
        -Member member
        -List~OrderLine~ orderlines
        +createOrder(Member member) Order
        +addOrderLine(Product product, Quantity quantity) void
        +getTotalAmount() Money
    }

    class OrderLine {
        -Money total
        -Product product
        -Quantity quantity
        +createItem(Product product, Quantity quantity) OrderLine
        +getLineTotal() Money
    }

    class Payment {
        -Money amount
        -PaymentMethod method
        +create(Order order) Payment
    }

    Member "1" --> "1" Point
    Member "1" <-- "0..*" Order
    Member "1" <-- "0..*" Heart
    Order "1" --> "1..*" OrderLine
    Order "1" <-- "1" Payment
    OrderLine "1" --> "1" Product
    Heart "0..*" --> "1" Product
    Product "0..*" --> "1" Brand
    
```