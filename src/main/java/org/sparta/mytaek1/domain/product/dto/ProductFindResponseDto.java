package org.sparta.mytaek1.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.sparta.mytaek1.domain.product.entity.Product;

@Getter
@Setter
public class ProductFindResponseDto {
    private Long productId;
    private String productName;
    private int productPrice;
    private int productStock;

    public ProductFindResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName =product.getProductName();
        this.productPrice = product.getProductPrice();
        this.productStock = product.getProductStock();
    }
}
