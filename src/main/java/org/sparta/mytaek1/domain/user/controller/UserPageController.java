package org.sparta.mytaek1.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;

    @GetMapping("/my-page")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        String userName = userDetails.getUser().getUserName();
        String userEmail = userDetails.getUser().getUserEmail();
        String streamKey = userDetails.getUser().getStreamKey();
        String userPhone = userDetails.getUser().getUserPhone();
        String userAddress = userDetails.getUser().getUserAddress();
        String postcode = userDetails.getUser().getPostcode();
        List<Broadcast> broadcastList = userService.getBroadcasts(userId);
        List<Orders> orderList = userService.getOrders(userId);

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("streamKey", streamKey);
        model.addAttribute("userPhone", userPhone);
        model.addAttribute("userAddress", userAddress);
        model.addAttribute("postcode", postcode);
        model.addAttribute("broadcastList", broadcastList);
        model.addAttribute("orderList", orderList);
        return "myPage";
    }

    @GetMapping("/api/user/login-page")
    public String signPage() {
        return "sign";
    }
}
