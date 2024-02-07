package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, String> redisTemplate;


    public OrderResponseDto createOrder(Long productId, OrderRequestDto orderRequestDto, User user) {
        Product product = productRepository.findById(productId).orElseThrow();

        // Redis를 사용하여 락 획득
        String lockKey = "order_creation_lock_" + productId;
        boolean lockAcquired = false;
        try {
            lockAcquired = acquireLock(lockKey);
            if (!lockAcquired) {
                throw new IllegalStateException("다른 사용자가 주문 생성 중입니다. 잠시 후 다시 시도해주세요.");
            }

            if (product.getProductStock() < orderRequestDto.getQuantity()) {
                throw new IllegalArgumentException("상품의 수량이 부족합니다.");
            }

            Orders order = new Orders(orderRequestDto, product, user, false);
            product.updateStock(orderRequestDto.getQuantity());
            log.info("주문이 생성되었습니다.");
            return new OrderResponseDto(orderRepository.save(order));
        } finally {
            // 락 해제
            if (lockAcquired) {
                releaseLock(lockKey);
                log.info("락이 해제되었습니다. Lock Key: {}", lockKey);
            }
        }
    }

    private boolean acquireLock(String lockKey) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Boolean lockAcquired = ops.setIfAbsent(lockKey, "LOCKED");
        if (lockAcquired != null && lockAcquired) {
            // 락 획득 성공
            redisTemplate.expire(lockKey, 30, TimeUnit.SECONDS); // 락 만료 시간 설정
            log.info("락을 획득하였습니다. Lock Key: {}", lockKey);
            return true;
        }
        // 락 획득 실패
        return false;
    }

    private void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
        log.info("releaseLock 호출(LockKey 제거)");
    }

//    @Transactional
//    public OrderResponseDto createOrder(Long productId, OrderRequestDto orderRequestDto, User user) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        if (product.getProductStock() < orderRequestDto.getQuantity()) {
//            throw new IllegalArgumentException("상품의 수량이 부족합니다.");
//        }
//            Orders order = new Orders(orderRequestDto, product, user, false);
//            product.updateStock(orderRequestDto.getQuantity());
//            return new OrderResponseDto(orderRepository.save(order));
//
//    }

    @Transactional
    public OrderResponseDto getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow();
        return new OrderResponseDto(order);
    }
}
