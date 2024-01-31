package org.sparta.mytaek1.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.BroadCast;
import org.sparta.mytaek1.domain.order.entity.Order;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.audit.Auditable;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private int productPrice;
    private int productStock;
}
