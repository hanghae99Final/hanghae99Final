package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BroadcastPageController {

    private final BroadcastService broadcastService;
    private final StockService stockService;
    private final UserService userService;
    @Value("${streaming.server.ip}")
    private String streamingIp;

    @GetMapping("/broadcasts/start")
    public String getBroadCastForm() {
        return "broadcastForm";
    }

    @GetMapping
    public String getBroadcastsOnAir(Model model, @PageableDefault(page = 0 ,size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<BroadcastResponseDto> broadcastResponseDtoPage = broadcastService.getAllBroadCast(pageable);
        model.addAttribute("broadcastResponseDtoPage", broadcastResponseDtoPage);
        return "broadcastList";
    }
  
    @GetMapping("/broadcasts/{broadcastId}")
    public String showBroadcast(@PathVariable Long broadcastId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Broadcast broadcast = broadcastService.getBroadcastByBroadcastId(broadcastId);
        Long productId = broadcast.getProduct().getProductId();
        Stock stock = stockService.findStockByProduct(productId);
      
        if (userDetails != null) {
            User user = userService.findByUserEmail(userDetails.getUsername());
            Long authenticatedUserId = user.getUserId();
            model.addAttribute("authenticatedUserId", authenticatedUserId);
        }

        if (userDetails == null) {
            model.addAttribute("authenticatedUserId", null);
        }

        model.addAttribute("broadcast", broadcast);
        model.addAttribute("stock", stock);
        model.addAttribute("serverIp", streamingIp);
        return "broadcast";
    }
}