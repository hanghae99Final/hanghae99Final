package org.sparta.mytaek1.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.stock.entity.Stock;

@Getter
@Setter
public class ProductFindResponseDto {
    private Long productId;
    private String productName;
    private int productPrice;
    private int productStock;

    public ProductFindResponseDto(Product product, Stock stock) {
        this.productId = product.getProductId();
        this.productName =product.getProductName();
        this.productPrice = product.getProductPrice();
        this.productStock = stock.getProductStock();
    }
}
