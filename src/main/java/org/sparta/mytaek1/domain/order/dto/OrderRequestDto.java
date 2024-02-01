package org.sparta.mytaek1.domain.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {
    private Integer quantity;

    public OrderRequestDto (Integer quantity) {
        this.quantity = quantity;
    }
}
