package org.sparta.mytaek1.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.mytaek1.domain.order.dto.OrderRequestDto;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.global.audit.Auditable;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Integer quantity;

    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(value = EnumType.STRING)
    private OrderState paymentStatus = OrderState.READY;

    private String merchantUid;

    public Orders(OrderRequestDto orderRequestDto, Product product, User user) {
        this.quantity = orderRequestDto.getQuantity();
        this.totalPrice = orderRequestDto.getTotalPrice();
        this.user = user;
        this.product = product;
    }

    public void update() {
        this.paymentStatus = OrderState.CONFIRM;
    }

    public void updateMerchant(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public void cancel() {
        this.paymentStatus = OrderState.CANCEL;
    }
}
