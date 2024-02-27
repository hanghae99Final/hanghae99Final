package org.sparta.mytaek1.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final StockService stockService;

    @PostMapping("/products/{productId}/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long productId,
                                              @RequestBody OrderRequestDto orderRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        OrderResponseDto orderResponseDto = orderService.createOrder(productId, orderRequestDto, user);
        return ResponseEntity.ok(orderResponseDto);
    }
}
