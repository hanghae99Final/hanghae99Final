package org.sparta.mytaek1.domain.order.repository;

import org.sparta.mytaek1.domain.order.entity.OrderState;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByProductProductId(Long productId);

    List<Orders> findByPaymentStatusAndCreatedAtBefore(OrderState orderState, LocalDateTime tenMinutesAgo);

    @Query("SELECT o FROM orders o JOIN o.product p WHERE o.user.userId = :userId")
    @EntityGraph(attributePaths = {"product"})
    Page<Orders> findAllByUserUserId(Long userId, Pageable pageable);

    @Query("SELECT o FROM orders o JOIN FETCH o.user WHERE o.orderId = :orderId")
    Optional<Orders> findOrderWithUserById(@Param("orderId") Long orderId);
}
