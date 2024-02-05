package org.sparta.mytaek1.domain.order;

import org.junit.jupiter.api.Test;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceTest {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceTest(UserRepository userRepository, OrderService orderService, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Test
    public void testCreateOrder() {
        // 테스트 데이터 설정
        Product product = new Product("육개장","맛있어요",1,94);
        productRepository.save(product); // 테스트용 Product 저장
        Long productId = product.getProductId();
        User user = new User("홍길동","bobo@email.com","Asdfqwer12@","8b14d0c3-e32c-4bea-af21-f0bc3d5d4c29","01012345678","경기도 화성시","12345");
        userRepository.save(user);
        // 테스트 메서드 실행
        OrderRequestDto orderRequestDto = new OrderRequestDto(1,1);
        orderService.createOrder(productId, orderRequestDto, user);

        // 생성된 주문 확인
        Orders savedOrder = orderRepository.findByProductAndUser(product, user);
        assertNotNull(savedOrder); // 주문이 잘 생성되었는지 확인
        assertEquals(orderRequestDto.getQuantity(), savedOrder.getQuantity()); // 주문 수량이 맞는지 확인
    }
}

