package org.sparta.mytaek1.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.repository.BroadcastRepository;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.order.entity.Orders;
import org.sparta.mytaek1.domain.order.repository.OrderRepository;
import org.sparta.mytaek1.domain.order.service.OrderService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final BroadcastService broadcastService;
    private final OrderService orderService;

    @GetMapping("/my-page")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails,
                         @RequestParam(name = "broadcastPage", defaultValue = "0",required = false) Integer  broadcastPageParam,
                         @RequestParam(name = "orderPage", defaultValue = "0",required = false) Integer  orderPageParam,
                         HttpSession session) {
        Integer broadcastPage = (broadcastPageParam != null) ? broadcastPageParam : (Integer) session.getAttribute("broadcastPage");
        Integer orderPage = (orderPageParam != null) ? orderPageParam : (Integer) session.getAttribute("orderPage");

        if (broadcastPage == null) broadcastPage = 0;
        if (orderPage == null) orderPage = 0;

        session.setAttribute("broadcastPage", broadcastPage);
        session.setAttribute("orderPage", orderPage);
        Long userId = userDetails.getId();
        String userName = userDetails.getUser().getUserName();
        String userEmail = userDetails.getUser().getUserEmail();
        String streamKey = userDetails.getUser().getStreamKey();
        String userPhone = userDetails.getUser().getUserPhone();
        String userAddress = userDetails.getUser().getUserAddress();
        String postcode = userDetails.getUser().getPostcode();

        Pageable broadcastPageable = PageRequest.of(broadcastPage, 10);
        Pageable orderPageable = PageRequest.of(orderPage, 10);

        Page<Broadcast> broadcastList = broadcastService.findBroadcastListByUserId(userId,broadcastPageable);
        Page<Orders> orderList = orderService.findOrderListByUserId(userId,orderPageable);

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

    @GetMapping("/api/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
