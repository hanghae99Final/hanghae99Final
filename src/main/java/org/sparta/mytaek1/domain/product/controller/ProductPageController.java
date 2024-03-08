package org.sparta.mytaek1.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/products/{productId}")
    public String findProduct(@PathVariable Long productId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("productFind", productService.getProduct(productId));

        if (userDetails != null) {
            User user = userService.findByUserEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "product";
    }
}
