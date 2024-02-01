package org.sparta.mytaek1.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductRequestDto {
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;
}
