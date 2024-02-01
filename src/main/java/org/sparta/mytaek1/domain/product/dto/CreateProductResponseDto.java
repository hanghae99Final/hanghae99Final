package org.sparta.mytaek1.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sparta.mytaek1.domain.product.entity.Product;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductResponseDto {
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;

    public CreateProductResponseDto(Product product) {
        this.productName = product.getProductName();
        this.productDescription = product.getProductDescription();
        this.productPrice = product.getProductPrice();
        this.productStock = product.getProductStock();
    }
}
