package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.stock.entity.Stock;
import org.sparta.mytaek1.domain.stock.service.StockService;
import org.sparta.mytaek1.global.page.dto.PageDto;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BroadcastPageController {

    private final BroadcastService broadcastService;
    private final StockService stockService;
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
    public String getBroadcastsOnAir(Model model,@PageableDefault(page = 0 ,size = 1, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<BroadcastResponseDto> broadcastResponseDtoPage = broadcastService.getAllBroadCast(pageable);
        model.addAttribute("broadcastResponseDtoPage", broadcastResponseDtoPage);
        return "broadcastList";
    }
    @GetMapping("/broadcasts/{broadcastId}")
    public String showBroadcast(@PathVariable Long broadcastId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Broadcast broadcast = broadcastService.getBroadcastByBroadcastId(broadcastId);
        Long productId = broadcast.getProduct().getProductId();
        Stock stock = stockService.findStockByProduct(productId);
        Long authenticatedUserId = (userDetails != null) ? userDetails.getId() : null;
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