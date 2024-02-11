package org.sparta.mytaek1.domain.broadcast;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Rollback
@AutoConfigureMockMvc
public class BroadcastServiceTest {

    private final BroadcastService broadcastService;

    private final UserRepository userRepository;

    private final BroadcastRepository broadcastRepository;

    private final ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public BroadcastServiceTest(BroadcastService broadcastService, UserRepository userRepository, ProductService productService, BroadcastRepository broadcastRepository, ProductRepository productRepository) {
        this.broadcastService = broadcastService;
        this.userRepository = userRepository;
        this.broadcastRepository = broadcastRepository;
        this.productRepository = productRepository;
    }

//    @Test
//    @Transactional
//    @DisplayName("Product, Broadcast 생성")
//    public void createBroadcastTest() {
//        User user = new User("Test User2", "test2@example.com", "password1234@", "streamKey", "123-456-7890", "Test Address", "12345");
//        userRepository.save(user);
//        UserDetailsImpl userDetails = new UserDetailsImpl(user);
//        BroadcastRequestDto requestDto = new BroadcastRequestDto("Test Title", "Test Description", "Test Product", "Product Description", 100, 10);
//        broadcastService.createBroadcast(userDetails, requestDto);
//        Broadcast broadcast = broadcastRepository.findByBroadcastTitle("Test Title");
//
//        assertEquals("Test Title", broadcast.getBroadcastTitle());
//        assertEquals("Test Description", broadcast.getBroadcastDescription());
//        assertEquals("Test User2", broadcast.getUser().getUserName());
//        assertEquals("Test Product", broadcast.getProduct().getProductName());
//        assertEquals(100, broadcast.getProduct().getProductPrice());
//        assertEquals(10, broadcast.getProduct().getProductStock());
//    }

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

//    @Test
//    @Transactional
//    @DisplayName("방송 화면 조회 테스트")
//    @Rollback
//    public void showBroadcastTest() throws Exception {
//
//        User user = new User("TestUser", "test@user.com", "Password123!", "TestStreamKey", "010-1234-1234", "ASDF", "12345");
//        Product product = new Product("TestProduct", "TestProductDescription", 10000, 10);
//        Broadcast broadcast = new Broadcast("TestBroadcast", "TestDescription", user, product);
//        userRepository.save(user);
//        productRepository.save(product);
//        broadcastRepository.save(broadcast);
//
//        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/broadcasts/" + broadcast.getBroadcastId()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("broadcast"))
//                .andReturn();
//
//        String returnedStreamKey = (String) Objects.requireNonNull(result.getModelAndView()).getModel().get("streamKey");
//        Product returnedProduct = (Product) result.getModelAndView().getModel().get("product");
//
//        Assertions.assertEquals(broadcast.getUser().getStreamKey(), returnedStreamKey);
//        Assertions.assertEquals(broadcast.getProduct().getProductName(), returnedProduct.getProductName());
//        Assertions.assertEquals(broadcast.getProduct().getProductDescription(), returnedProduct.getProductDescription());
//        Assertions.assertEquals(broadcast.getProduct().getProductPrice(), returnedProduct.getProductPrice());
//        Assertions.assertEquals(broadcast.getProduct().getProductStock(), returnedProduct.getProductStock());
//    }
}