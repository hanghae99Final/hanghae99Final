package org.sparta.mytaek1.domain.broadcast.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.user.entity.User;

@Getter
@RequiredArgsConstructor
public class BroadcastRequestDto {

    private final String broadcastTitle;
    private final String broadcastDescription;

    public Broadcast toEntity(User user, Product product) {
        return Broadcast.builder()
                .broadcastTitle(this.broadcastTitle)
                .broadcastDescription(this.broadcastDescription)
                .onAir(true)
                .user(user)
                .product(product)
                .build();
    }
}
