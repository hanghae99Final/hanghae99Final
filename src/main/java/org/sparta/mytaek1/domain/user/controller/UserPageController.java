package org.sparta.mytaek1.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final BroadcastService broadcastService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/my-page")
    public String myPage(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "broadcastPage", defaultValue = "0",required = false) Integer  broadcastPageParam,
                         @RequestParam(name = "orderPage", defaultValue = "0",required = false) Integer  orderPageParam,
                         HttpSession session) {
        Integer broadcastPage = (broadcastPageParam != null) ? broadcastPageParam : (Integer) session.getAttribute("broadcastPage");
        Integer orderPage = (orderPageParam != null) ? orderPageParam : (Integer) session.getAttribute("orderPage");

        if (broadcastPage == null) broadcastPage = 0;
        if (orderPage == null) orderPage = 0;

        User user = userService.findByUserEmail(userDetails.getUsername());
        Long userId = user.getUserId();

        Page<Broadcast> broadcastList = broadcastService.findBroadcastListByUserId(userId, PageRequest.of(broadcastPage, 10));
        Page<Orders> orderList = orderService.findOrderListByUserId(userId, PageRequest.of(orderPage, 10));

        session.setAttribute("broadcastPage", broadcastPage);
        session.setAttribute("orderPage", orderPage);
        model.addAttribute("user", user);
        model.addAttribute("broadcastList", broadcastList);
        model.addAttribute("orderList", orderList);
        return "myPage";
    }

    @GetMapping("/api/user/login-page")
    public String signPage() {
        return "sign";
    }

    @GetMapping("/api/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }
}
