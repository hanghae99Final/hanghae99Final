package org.sparta.mytaek1.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final ProductService productService;

    @GetMapping("/products/{productId}")
    public String findProduct(@PathVariable Long productId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("productFind", productService.findProduct(productId));

        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }

        return "product";
    }
}
