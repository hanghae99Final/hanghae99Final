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
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    private final BroadcastRepository broadcastRepository;
    private final StockService stockService;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public BroadcastServiceTest(BroadcastService broadcastService, UserRepository userRepository, ProductService productService, BroadcastRepository broadcastRepository, ProductRepository productRepository, StockService stockService, StockRepository stockRepository) {
        this.broadcastService = broadcastService;
        this.userRepository = userRepository;
        this.broadcastRepository = broadcastRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
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
        Stock stock = stockService.findStockByProduct(broadcast.getProduct().getProductId());

        assertEquals("Test Title", broadcast.getBroadcastTitle());
        assertEquals("Test Description", broadcast.getBroadcastDescription());
        assertEquals("Test User2", broadcast.getUser().getUserName());
        assertEquals("Test Product", broadcast.getProduct().getProductName());
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
        Product product = new Product("TestProduct", "TestProductDescription", 10000);

        Broadcast broadcast = new Broadcast("TestBroadcast", "TestDescription", user, product);
        userRepository.save(user);
        productRepository.save(product);
        broadcastRepository.save(broadcast);
        Stock stock = new Stock(product, 100);
        stockRepository.save(stock);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/broadcasts/" + broadcast.getBroadcastId()))
                .andExpect(status().isOk())
                .andExpect(view().name("broadcast"))
                .andReturn();

        String returnedStreamKey = (String) Objects.requireNonNull(result.getModelAndView()).getModel().get("streamKey");
        Product returnedProduct = (Product) result.getModelAndView().getModel().get("product");

        Stock returnedStock = stockRepository.findByProductProductId(returnedProduct.getProductId()).orElseThrow();
        Stock broadcastStock = stockRepository.findByProductProductId(broadcast.getProduct().getProductId()).orElseThrow();

        Assertions.assertEquals(broadcast.getUser().getStreamKey(), returnedStreamKey);
        Assertions.assertEquals(broadcast.getProduct().getProductName(), returnedProduct.getProductName());
        Assertions.assertEquals(broadcast.getProduct().getProductDescription(), returnedProduct.getProductDescription());
        Assertions.assertEquals(broadcast.getProduct().getProductPrice(), returnedProduct.getProductPrice());
        Assertions.assertEquals(broadcastStock.getProductStock(), returnedStock.getProductStock());
    }
}