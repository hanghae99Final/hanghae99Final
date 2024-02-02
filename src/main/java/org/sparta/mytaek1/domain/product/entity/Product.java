package org.sparta.mytaek1.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Builder
    public Product(Long productId, String productName,String productDescription, int productPrice, int productStock) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    public Product(String productName, String productDescription, int productPrice, int productStock) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }
    public void updateStock(int productStock){
        this.productStock = this.productStock - productStock;
    }
}
