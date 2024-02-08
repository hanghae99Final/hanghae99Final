package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.config.RedisConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient = RedisConfig.getRedissonClient();

    public OrderResponseDto createOrder(Long productId, OrderRequestDto orderRequestDto, User user) {
        RLock fairLock = redissonClient.getFairLock("pro:" + productId);
        Product product = productRepository.findById(productId).orElseThrow();
        Orders order = new Orders(orderRequestDto, product, user, false);
        System.out.println("하이요");
        try{
            if(fairLock.tryLock(1000000, TimeUnit.SECONDS)){
                System.out.println("겟락");
                product.updateStock(orderRequestDto.getQuantity());
                orderRepository.save(order);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                System.out.println("드랍락");
                fairLock.unlock();
            }

        }
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow();
        return new OrderResponseDto(order);
    }
}
