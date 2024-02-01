package org.sparta.mytaek1.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.product.dto.*;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductFindResponseDto findProduct(Long productId) {
        return new ProductFindResponseDto(productRepository.findById(productId).orElseThrow(()-> new IllegalArgumentException("상품 정보가 없습니다.")));
    }

//    public CreateProductResponseDto createProduct(CreateProductRequestDto requestDto) {
//        Product product = new Product(requestDto);
//        productRepository.save(product);
//        return new CreateProductResponseDto(product);
//    }

    //TODO:: 레디스 기반 업데이트 추가 필요
    public UpdateProductStockResponseDto updateProductStock(UpdateProductStockRequestDto requestDto) {
        return null;
    }

    public Product createProduct(BroadcastRequestDto requestDto) {
        Product product = new Product(requestDto.getProductName(), requestDto.getProductDescription(), requestDto.getProductPrice(), requestDto.getProductStock());

        productRepository.save(product);

        return product;
    }
}
