package org.sparta.mytaek1.domain.order;

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
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private Product product;
    private Stock stock;
    private User user;

    @Autowired
    public OrderServiceTest(UserRepository userRepository, OrderService orderService, ProductRepository productRepository, OrderRepository orderRepository, StockRepository stockRepository, StockService stockService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
    }

    @BeforeEach
    void setUp() {
        product = new Product("고구마", "호박고구망", 10000);
        productRepository.save(product);
        stock = new Stock(product, 100);
        stockRepository.save(stock);
    }

    @Test
    void 재고차감_분산락_적용_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        user = new User("다보미","da125434@email.com","asdf1234!","1234456","01012345678","경기도 파주시","12345");
        userRepository.save(user);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    Long productId = product.getProductId();
                    OrderRequestDto orderRequestDto = new OrderRequestDto(1,1);
                    Stock stock1 = stockService.findStockById(product.getProductId());
                    orderService.createOrder(stock1.getStockId(), productId, orderRequestDto, user);
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
