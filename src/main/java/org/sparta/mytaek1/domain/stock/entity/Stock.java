package org.sparta.mytaek1.domain.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.global.audit.Auditable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;
    private int productStock;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Stock(Product product, int productStock) {
        this.product = product;
        this.productStock = productStock;
    }

    public void updateStock(int productStock){
        this.productStock = this.productStock - productStock;
    }
}
