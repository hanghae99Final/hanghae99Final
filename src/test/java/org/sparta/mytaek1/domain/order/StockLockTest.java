package org.sparta.mytaek1.domain.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.repository.StockRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class StockLockTest {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private Product product;
    private Stock stock;
    private User user;

    @Autowired
    public StockLockTest(UserRepository userRepository, OrderService orderService, ProductRepository productRepository, OrderRepository orderRepository, StockRepository stockRepository) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
    }

    @BeforeEach
    void setUp() {
        product = new Product("고구마", "호박고구망", 10000);
        productRepository.save(product);
        stock = new Stock(product, 100);
        stockRepository.save(stock);
    }

    /**
     * Feature: 쿠폰 차감 동시성 테스트
     * Background
     *     Given KURLY_001 라는 이름의 쿠폰 100장이 등록되어 있음
     * <p>
     * Scenario: 100장의 쿠폰을 100명의 사용자가 동시에 접근해 발급 요청함
     *           Lock의 이름은 쿠폰명으로 설정함
     * <p>
     * Then 사용자들의 요청만큼 정확히 쿠폰의 개수가 차감되어야 함
     */

    @Test
//    @Transactional
    void 재고차감_분산락_적용_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        user = new User("다보미","da123@email.com","asdf1234!","123477756","01012345678","경기도 파주시","12345");
        userRepository.save(user);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    Long productId = product.getProductId();
                    OrderRequestDto orderRequestDto = new OrderRequestDto(1,1);
                    orderService.createOrder(user.getUserName(), productId, orderRequestDto, user);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        List<Orders> orders = orderRepository.findAllByProductProductId(product.getProductId());
        int numberOfOrders = orders.size();
        Stock persistStock = stockRepository.findById(stock.getStockId())
                  .orElseThrow(IllegalArgumentException::new);

        assertThat(numberOfOrders).isEqualTo(100);
        assertThat(persistStock.getProductStock()).isZero();
    }
}
