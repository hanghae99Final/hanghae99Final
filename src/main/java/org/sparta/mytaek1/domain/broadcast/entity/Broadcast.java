package org.sparta.mytaek1.domain.broadcast.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    //생성자 빌더는 나중에 다 풀받고

}
