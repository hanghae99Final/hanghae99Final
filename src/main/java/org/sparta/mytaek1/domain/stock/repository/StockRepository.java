package org.sparta.mytaek1.domain.stock.repository;

import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductProductId(Long productId);
}
