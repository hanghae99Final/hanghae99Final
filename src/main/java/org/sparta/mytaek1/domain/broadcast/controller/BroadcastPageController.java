package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BroadcastPageController {

    private final BroadcastService broadcastService;
    private final StockService stockService;
    private final UserRepository userRepository;
    @Value("${streaming.server.ip}")
    private String streamingIp;

    @GetMapping("/broadcasts")
    public String showBroadcast() {
        return "broadcast";
    }

    @GetMapping("/broadcasts/start")
    public String getBroadCastForm() {
        return "broadcastForm";
    }

    @GetMapping
    @PreAuthorize("permitAll")
    public String getBroadcastsOnAir(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        log.info("Email : " + userDetails.getUsername());
//        log.info("Authorities : " + userDetails.getAuthorities());
        List<BroadcastResponseDto> broadcastResponseDtoList = broadcastService.getAllBroadCast();
        model.addAttribute("broadcastResponseDtoList", broadcastResponseDtoList);
        return "broadcastList";
    }

    @GetMapping("/broadcasts/{broadcastId}")
    public String showBroadcast(@PathVariable Long broadcastId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Broadcast broadcast = broadcastService.getBroadcastByBroadcastId(broadcastId);
        Long productId = broadcast.getProduct().getProductId();
        Stock stock = stockService.findStockByProduct(productId);
        User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow();
        Long authenticatedUserId = (userDetails != null) ? user.getUserId() : null;
        Long broadcasterUserId = broadcast.getUser().getUserId();

        model.addAttribute("broadcastId", broadcast.getBroadcastId());
        model.addAttribute("authenticatedUserId", authenticatedUserId);
        model.addAttribute("broadcasterUserId", broadcasterUserId);
        model.addAttribute("streamKey", broadcast.getUser().getStreamKey());
        model.addAttribute("product", broadcast.getProduct());
        model.addAttribute("imageUrl", broadcast.getProduct().getImageUrl());
        model.addAttribute("stock", stock);
        model.addAttribute("serverIp", streamingIp);
        return "broadcast";
    }
}