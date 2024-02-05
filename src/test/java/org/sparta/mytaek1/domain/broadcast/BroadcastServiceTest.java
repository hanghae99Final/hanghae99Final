package org.sparta.mytaek1.domain.broadcast;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Rollback
public class BroadcastServiceTest {

    private final BroadcastService broadcastService;

    private final UserRepository userRepository;

    private final ProductService productService;

    private final BroadcastRepository broadcastRepository;

    @Autowired
    public BroadcastServiceTest(BroadcastService broadcastService, UserRepository userRepository, ProductService productService, BroadcastRepository broadcastRepository) {
        this.broadcastService = broadcastService;
        this.userRepository = userRepository;
        this.productService = productService;
        this.broadcastRepository = broadcastRepository;
    }

    @Test
    @Transactional
    @DisplayName("Product, Broadcast 생성")
    public void createBroadcastTest() {
        User user = new User("Test User2", "test2@example.com", "password1234@", "streamKey", "123-456-7890", "Test Address", "12345");
        userRepository.save(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        BroadcastRequestDto requestDto = new BroadcastRequestDto("Test Title", "Test Description", "Test Product", "Product Description", 100, 10);
        broadcastService.createBroadcast(userDetails, requestDto);
        Broadcast broadcast = broadcastRepository.findByBroadcastTitle("Test Title");

        assertEquals("Test Title", broadcast.getBroadcastTitle());
        assertEquals("Test Description", broadcast.getBroadcastDescription());
        assertEquals("Test User2", broadcast.getUser().getUserName());
        assertEquals("Test Product", broadcast.getProduct().getProductName());
        assertEquals(100, broadcast.getProduct().getProductPrice());
        assertEquals(10, broadcast.getProduct().getProductStock());
    }

    @Test
    @DisplayName("Null 체크")
    public void testNonNullFields() {
        // 유효한 데이터로 DTO 생성
        BroadcastRequestDto dto = new BroadcastRequestDto("Title", "Description", "Product", "Product Description", 100, 10);

        // 생성된 DTO의 필드가 null이 아닌지 확인
        assertNotNull(dto.getBroadcastTitle());
        assertNotNull(dto.getBroadcastDescription());
        assertNotNull(dto.getProductName());
        assertNotNull(dto.getProductDescription());
    }

    @Test
    @DisplayName("NullPointerException 예외 체크")
    public void testNullFields() {
        // null 값을 포함하는 데이터로 DTO 생성 (예외 발생을 기대)
        assertThrows(NullPointerException.class, () -> {
            BroadcastRequestDto dto = new BroadcastRequestDto(null, null, null, null, 100, 100);
        });
    }
}