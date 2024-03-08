package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.OrderState;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.sparta.mytaek1.global.redis.DistributedLock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final StockService stockService;

    @DistributedLock(key = "#productId")
    public OrderResponseDto createOrder(Long productId, OrderRequestDto orderRequestDto, User user) {
        Stock stock = stockService.findStockWithProduct(productId);
        Product product = stock.getProduct();

        if (stock.getProductStock() < orderRequestDto.getQuantity()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_STOCK_ERROR_MESSAGE.getErrorMessage());
        }

        Orders order = new Orders(orderRequestDto, product, user);
        stock.updateStock(orderRequestDto.getQuantity());
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void scheduledDeleteOrder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesAgo = now.minusMinutes(10);
        List<Orders> ordersToDelete = orderRepository.findByPaymentStatusAndCreatedAtBefore(OrderState.READY, tenMinutesAgo);

        for (Orders order : ordersToDelete) {
            Stock stock = stockService.findStockByProduct(order.getProduct().getProductId());
            deleteOrder(order.getProduct().getProductId(), order, stock);
        }
    }

    @Transactional
    public void cancelPaymentStatus(Long orderId) {
        Orders order = findOrderById(orderId);
        order.cancel();
        Stock stock = stockService.findStockByProduct(order.getProduct().getProductId());
        deleteOrder(order.getProduct().getProductId(),order,stock);
    }

    @Transactional
    public void updateMerchant(Long orderId,String merchantUid) {
        Orders orders = findOrderById(orderId);
        orders.updateMerchant(merchantUid);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long orderId) {
        Orders order = orderRepository.findOrderWithUserById(orderId).orElseThrow(()-> new NullPointerException(ErrorMessage.NOT_EXIST_ORDER_ERROR_MESSAGE.getErrorMessage()));
        return new OrderResponseDto(order);
    }

    @DistributedLock(key = "#productId")
    private void deleteOrder(Long productId, Orders order, Stock stock) {
        stock.cancelStock(order.getQuantity());
        orderRepository.delete(order);
    }

    public Orders findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new NullPointerException(ErrorMessage.NOT_EXIST_ORDER_ERROR_MESSAGE.getErrorMessage()));
    }

    public Page<Orders> findOrderListByUserId(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserUserId(userId,pageable);
    }
}

