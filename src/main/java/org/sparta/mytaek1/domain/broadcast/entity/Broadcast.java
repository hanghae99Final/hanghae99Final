package org.sparta.mytaek1.domain.broadcast.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.audit.Auditable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Broadcast extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long broadcastId;

    private String broadcastTitle;

    private String broadcastDescription;

    private boolean onAir;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Broadcast(Long broadcastId, String broadcastTitle, String broadcastDescription, boolean onAir, User user, Product product) {
        this.broadcastId = broadcastId;
        this.broadcastTitle = broadcastTitle;
        this.broadcastDescription = broadcastDescription;
        this.onAir = onAir;
        this.user = user;
        this.product = product;
    }

    public void endBroadcast() {
        this.onAir = false;
    }
}
