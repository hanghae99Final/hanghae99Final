package org.sparta.mytaek1.domain.stock.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Stock findStockByProduct(Long productId) {
        return stockRepository.findByProductProductId(productId).orElseThrow();
    }
}
