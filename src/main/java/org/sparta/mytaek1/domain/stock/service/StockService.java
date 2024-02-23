package org.sparta.mytaek1.domain.stock.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.product.entity.Product;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.repository.StockRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void createStock(Product product, int productStock) {
        Stock stock = new Stock(product, productStock);
        stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public Stock findStockByProduct(Long productId) {
        return stockRepository.findByProductProductId(productId).orElseThrow(()-> new IllegalArgumentException(ErrorMessage.NOT_EXIST_PRODUCT_ERROR_MESSAGE.getErrorMessage()));
    }

    @Transactional(readOnly = true)
    public Stock findStockById(Long productId) {
        return stockRepository.findByProductProductId(productId).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_STOCK_ERROR_MESSAGE.getErrorMessage()));
    }

    @Transactional(readOnly = true)
    public Stock findStockWithProduct(Long stockId) {
        return stockRepository.findStockWithProduct(stockId);
    }

}
