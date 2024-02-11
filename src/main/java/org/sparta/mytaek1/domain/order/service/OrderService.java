package org.sparta.mytaek1.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.dto.OrderResponseDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.repository.StockRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.config.RedisConfig;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.sparta.mytaek1.global.redis.DistributedLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final RedissonClient redissonClient = RedisConfig.getRedissonClient();

//    public OrderResponseDto createOrder(Long productId, OrderRequestDto orderRequestDto, User user) {
//        RLock fairLock = redissonClient.getFairLock("pro:" + productId);
//        Product product = productRepository.findById(productId).orElseThrow();
//        Stock stock = stockRepository.findByProductProductId(productId).orElseThrow();
//        Orders order = new Orders(orderRequestDto, product, user, false);
//        System.out.println("하이요");
//        try{
//            if(fairLock.tryLock(1000000, TimeUnit.SECONDS)){
//                System.out.println("겟락");
//                stock.updateStock(orderRequestDto.getQuantity());
//                orderRepository.save(order);
//                Thread.sleep(100);
//            }
//        } catch (InterruptedException e) {
//            System.out.println(e);
//            throw new RuntimeException(e);
//        } finally {
//            if (fairLock.isHeldByCurrentThread()) {
//                System.out.println("드랍락");
//                fairLock.unlock();
//            }
//
//        }
//        return new OrderResponseDto(order);
//    }

    @DistributedLock(key = "#lockName")
    public OrderResponseDto createOrder(String lockName, Long productId, OrderRequestDto orderRequestDto, User user) {
        Product product = findProduct(productId);
        Stock stock = findStock(productId);
        Orders order = new Orders(orderRequestDto, product, user, false);
        stock.updateStock(orderRequestDto.getQuantity());
        orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow();
        return new OrderResponseDto(order);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_PRODUCT_ERROR_MESSAGE.getErrorMessage()));
    }

    private Stock findStock(Long productId) {
        return stockRepository.findByProductProductId(productId).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_STOCK_ERROR_MESSAGE.getErrorMessage()));
    }
}
