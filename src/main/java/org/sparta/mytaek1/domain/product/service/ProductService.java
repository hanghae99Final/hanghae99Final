package org.sparta.mytaek1.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.product.dto.ProductFindResponseDto;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockService stockService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public ProductFindResponseDto getProduct(Long productId) {
        Stock stock = stockService.findStockWithProduct(productId);
        Product product = stock.getProduct();

        return new ProductFindResponseDto(product, stock);
    }

    @Transactional
    public Product createProduct(BroadcastRequestDto requestDto, MultipartFile imageFile) {
        String imageUrl = imageService.uploadImage(imageFile);
        Product product = new Product(requestDto.getProductName(), requestDto.getProductDescription(), requestDto.getProductPrice(), imageUrl);
        productRepository.save(product);
        stockService.createStock(product, requestDto.getProductStock());

        return product;
    }
}
