package org.sparta.mytaek1.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final ProductService productService;

    @GetMapping("/products/{productId}")
    public String findProduct(@PathVariable Long productId, Model model){
        model.addAttribute("productFind",productService.findProduct(productId));
        return "product";
    }
}
