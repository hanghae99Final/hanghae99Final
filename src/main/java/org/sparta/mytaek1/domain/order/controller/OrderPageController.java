package org.sparta.mytaek1.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class OrderPageController {

    private final OrderService orderService;
    private final ProductService productService;


    @GetMapping("/products/{productId}/orders/{orderId}")
    public String orderPage(@PathVariable Long productId, @PathVariable Long orderId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("product", productService.findProduct(productId));
        model.addAttribute("order", orderService.getOrder(orderId));

        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }

        return "order";
    }
}
