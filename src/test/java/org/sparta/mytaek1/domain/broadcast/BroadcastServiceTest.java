package org.sparta.mytaek1.domain.broadcast;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.repository.StockRepository;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@Rollback
@AutoConfigureMockMvc
public class BroadcastServiceTest {

    private final BroadcastService broadcastService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BroadcastRepository broadcastRepository;
    private final StockService stockService;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public BroadcastServiceTest(BroadcastService broadcastService, UserRepository userRepository, ProductService productService, BroadcastRepository broadcastRepository, ProductRepository productRepository, StockService stockService, StockRepository stockRepository, UserService userService) {
        this.broadcastService = broadcastService;
        this.userRepository = userRepository;
        this.broadcastRepository = broadcastRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
        this.userService = userService;
    }

    @Test
    @Transactional
    @DisplayName("Product, Broadcast 생성")
    public void createBroadcastTest() {
        User user = new User("Test User2", "test2@example.com", "password1234@", "streamKey", "123-456-7890", "Test Address", "12345");
        userRepository.save(user);
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test data".getBytes());

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        BroadcastRequestDto requestDto = new BroadcastRequestDto("Test Title", "Test Description", "Test Product", "Product Description", 100, 10);
        broadcastService.createBroadcast(userDetails, requestDto, imageFile);
        Broadcast broadcast = broadcastRepository.findByBroadcastTitle("Test Title");
        Stock stock = stockService.findStockByProduct(broadcast.getProduct().getProductId());

        assertEquals("Test Title", broadcast.getBroadcastTitle());
        assertEquals("Test Description", broadcast.getBroadcastDescription());
        assertEquals("Test User2", broadcast.getUser().getUserName());
        assertEquals("Test Product", broadcast.getProduct().getProductName());
        assertTrue(broadcast.getProduct().getImageUrl().startsWith("https://seungbaeimage.s3.ap-northeast-2.amazonaws.com/"));
        assertEquals(100, broadcast.getProduct().getProductPrice());
        assertEquals(10, stock.getProductStock());
    }

    @Test
    @DisplayName("Null 체크")
    public void testNonNullFields() {
        BroadcastRequestDto dto = new BroadcastRequestDto("Title", "Description", "Product", "Product Description", 100, 10);

        assertNotNull(dto.getBroadcastTitle());
        assertNotNull(dto.getBroadcastDescription());
        assertNotNull(dto.getProductName());
        assertNotNull(dto.getProductDescription());
    }

    @Test
    @DisplayName("NullPointerException 예외 체크")
    public void testNullFields() {
        assertThrows(NullPointerException.class, () -> {
            BroadcastRequestDto dto = new BroadcastRequestDto(null, null, null, null, 100, 100);
        });
    }

    @Test
    @Transactional
    @DisplayName("방송 화면 조회 테스트")
    @Rollback
    public void showBroadcastTest() throws Exception {
        User user = new User("TestUser", "test@user.com", "Password123!", "TestStreamKey", "010-1234-1234", "ASDF", "12345");
        Product product = new Product("TestProduct", "TestProductDescription", 10000, "https://seungbae-lee.s3.ap-northeast-2.amazonaws.com/eb930357-38ef-45eb-b5dc-4cf9c629ef29.png");
        Broadcast broadcast = new Broadcast("TestBroadcast", "TestDescription", user, product);
        Stock stock = new Stock(product, 100);

        userRepository.save(user);
        productRepository.save(product);
        broadcastRepository.save(broadcast);
        stockRepository.save(stock);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/broadcasts/" + broadcast.getBroadcastId()))
                .andExpect(status().isOk())
                .andExpect(view().name("broadcast"))
                .andReturn();

        Broadcast returnedBroadcast = (Broadcast) Objects.requireNonNull(result.getModelAndView()).getModel().get("broadcast");
        Product returnedProduct = returnedBroadcast.getProduct();

        Stock returnedStock = stockRepository.findByProductProductId(returnedProduct.getProductId()).orElseThrow();
        Stock broadcastStock = stockRepository.findByProductProductId(broadcast.getProduct().getProductId()).orElseThrow();

        Assertions.assertAll(
                () -> assertEquals(broadcast.getProduct().getProductName(), returnedProduct.getProductName()),
                () -> assertEquals(broadcast.getProduct().getProductDescription(), returnedProduct.getProductDescription()),
                () -> assertEquals(broadcast.getProduct().getProductPrice(), returnedProduct.getProductPrice()),
                () -> assertEquals(broadcastStock.getProductStock(), returnedStock.getProductStock())
        );
    }
}