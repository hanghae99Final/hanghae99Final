package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.sparta.mytaek1.global.redis.DistributedLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final StockService stockService;

    @DistributedLock(key = "#lockName")
    public OrderResponseDto createOrder(Long lockName, Long productId, OrderRequestDto orderRequestDto, User user) {
        Product product = productService.findProduct(productId);
        Stock stock = stockService.findStockById(productId);

        if (stock.getProductStock() < orderRequestDto.getQuantity()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_STOCK_ERROR_MESSAGE.getErrorMessage());
        }

        Orders order = new Orders(orderRequestDto, product, user, false);
        stock.updateStock(orderRequestDto.getQuantity());
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void scheduledDeleteOrder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesAgo = now.minusMinutes(10);
        List<Orders> ordersToDelete = orderRepository.findByPaymentStatusAndCreatedAtBefore(false, tenMinutesAgo);

        for (Orders order : ordersToDelete) {
            if (order.getCreatedAt().plusMinutes(10).isBefore(now)) {
                Stock stock = stockService.findStockByProduct(order.getProduct().getProductId());
                stock.cancelStock(order.getQuantity());
                orderRepository.delete(order);
            }
        }
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(()-> new NullPointerException(ErrorMessage.NOT_EXIST_ORDER_ERROR_MESSAGE.getErrorMessage()));
        return new OrderResponseDto(order);
    }

    public Orders findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new NullPointerException(ErrorMessage.NOT_EXIST_ORDER_ERROR_MESSAGE.getErrorMessage()));
    }

//    @DistributedLock(key = "#lockName")
//    private void deleteOrder(Long lockName, Orders order, Stock stock) {
//        stock.cancelStock(order.getQuantity());
//        orderRepository.delete(order);
//    }
}
