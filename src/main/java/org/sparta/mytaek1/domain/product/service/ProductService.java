package org.sparta.mytaek1.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.product.dto.ProductFindResponseDto;
import org.sparta.mytaek1.domain.product.dto.UpdateProductStockRequestDto;
import org.sparta.mytaek1.domain.product.dto.UpdateProductStockResponseDto;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ProductFindResponseDto findProduct(Long productId) {
        Stock stock = stockRepository.findByProductProductId(productId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow(()-> new IllegalArgumentException("상품 정보가 없습니다."));
        return new ProductFindResponseDto(product, stock);
    }

    public UpdateProductStockResponseDto updateProductStock(UpdateProductStockRequestDto requestDto) {
        return null;
    }

    public Product createProduct(BroadcastRequestDto requestDto) {
        Product product = new Product(requestDto.getProductName(), requestDto.getProductDescription(), requestDto.getProductPrice());
        Stock stock = new Stock(product, requestDto.getProductStock());

        productRepository.save(product);
        stockRepository.save(stock);

        return product;
    }
}
