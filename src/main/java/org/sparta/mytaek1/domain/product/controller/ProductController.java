package org.sparta.mytaek1.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.product.dto.CreateProductRequestDto;
import org.sparta.mytaek1.domain.product.dto.CreateProductResponseDto;
import org.sparta.mytaek1.domain.product.dto.UpdateProductStockRequestDto;
import org.sparta.mytaek1.domain.product.dto.UpdateProductStockResponseDto;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public CreateProductResponseDto createProduct(@RequestBody CreateProductRequestDto requestDto){
        return productService.createProduct(requestDto);
    }

    @PutMapping("/api/products/{productId}/stock")
    public UpdateProductStockResponseDto updateProductStock(@RequestBody UpdateProductStockRequestDto requestDto){
        return productService.updateProductStock(requestDto);
    }
}
