package org.sparta.mytaek1.domain.order.dto;

import lombok.Getter;
import org.sparta.mytaek1.domain.order.entity.Orders;

@Getter
public class OrderResponseDto {

    private final Long orderId;
    private final Integer quantity;
    private final Integer totalPrice;
    private final String userName;
    private final String productName;

    public OrderResponseDto(Orders order) {
        this.orderId = order.getOrderId();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.userName = order.getUser().getUserName();
        this.productName = order.getProduct().getProductName();
    }
}
