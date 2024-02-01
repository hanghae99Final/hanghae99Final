package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(OrderRequestDto requestDto) {
        int quantity = requestDto.getQuantity();

        Orders order = orderRepository.save(new Orders(quantity));
        new OrderResponseDto(order);
    }
}
