package org.sparta.mytaek1.domain.broadcast.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.user.entity.User;

@Getter
@RequiredArgsConstructor
public class testRequestDto {

    private final String broadcastTitle;
    private final String broadcastDescription;
    private final String productName;
    private final int productPrice;
    private final int productStock;

    public Broadcast broadcastTest(User user, Product product) {
        return Broadcast.builder()
                .broadcastTitle(this.broadcastTitle)
                .broadcastDescription(this.broadcastDescription)
                .onAir(true)
                .user(user)
                .product(product)
                .build();
    }

    public Product productTest() {
        return Product.builder()
                .productName(this.productName)
                .productPrice(this.productPrice)
                .productStock(this.productStock)
                .build();
    }
}
