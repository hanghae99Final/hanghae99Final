package org.sparta.mytaek1.domain.order.repository;

import org.sparta.mytaek1.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByProductProductId(Long productId);

    List<Orders> findByPaymentStatusAndCreatedAtBefore(boolean status, LocalDateTime tenMinutesAgo);

    @Query("SELECT o FROM orders o LEFT JOIN FETCH o.product p WHERE o.user.userId = :userId")
    List<Orders> findAllByUserUserId(Long userId);
}
