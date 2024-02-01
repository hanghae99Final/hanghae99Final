package org.sparta.mytaek1.domain.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.mytaek1.domain.product.dto.CreateProductRequestDto;
import org.sparta.mytaek1.global.audit.Auditable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;

    public Product(CreateProductRequestDto requestDto) {
        this.productName = requestDto.getProductName();
        this.productDescription = requestDto.getProductDescription();
        this.productPrice = requestDto.getProductPrice();
        this.productStock = requestDto.getProductStock();
    }
}
