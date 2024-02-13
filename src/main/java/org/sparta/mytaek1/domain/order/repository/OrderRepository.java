package org.sparta.mytaek1.domain.order.repository;

import org.sparta.mytaek1.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByProductProductId(Long productId);

    List<Orders> findByPaymentStatusAndCreatedAtBefore(boolean status, LocalDateTime tenMinutesAgo);
}
