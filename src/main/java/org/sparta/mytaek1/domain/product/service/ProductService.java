package org.sparta.mytaek1.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.product.dto.product.ProductFindResponseDto;
import org.sparta.mytaek1.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductFindResponseDto findProduct(Long productId) {
        return new ProductFindResponseDto(productRepository.findById(productId).orElseThrow(()-> new IllegalArgumentException("상품 정보가 없습니다.")));
    }
}
