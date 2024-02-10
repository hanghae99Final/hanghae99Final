package org.sparta.mytaek1.domain.order.repository;

import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByProductAndUser(Product product, User user);

    Optional<Orders> findByProduct(Product product);
}
