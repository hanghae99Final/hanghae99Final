package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public void createOrder(Long productId, OrderRequestDto orderRequestDto, User user) {
        Product product = productRepository.findById(productId).orElseThrow();
        Orders order = new Orders(orderRequestDto,product, user);
        orderRepository.save(order);
    }

//    @Transactional
//    public void createOrder(OrderRequestDto requestDto) {
//        int quantity = requestDto.getQuantity();
//
//        Orders order = orderRepository.save(new Orders(quantity));
//        new OrderResponseDto(order);
//    }
}
