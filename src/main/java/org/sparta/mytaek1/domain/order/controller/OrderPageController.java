package org.sparta.mytaek1.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.product.service.ProductService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class OrderPageController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserRepository userRepository;

    @GetMapping("/products/{productId}/orders/{orderId}")
    public String orderPage(@PathVariable Long productId, @PathVariable Long orderId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("product", productService.getProduct(productId));
        model.addAttribute("order", orderService.getOrder(orderId));
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();

        if (userDetails != null) {
            model.addAttribute("user", user);
        }

        return "order";
    }
}
