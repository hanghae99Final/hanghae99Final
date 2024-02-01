package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BroadcastPageController {
    private final BroadcastService broadcastService;

    @GetMapping("/broadcasts/{broadcastId}")
    public String showBroadcast(@PathVariable Long broadcastId, Model model) {
        Broadcast broadcast = broadcastService.getBroadcastByBroadcastId(broadcastId);
        model.addAttribute("streamKey", broadcast.getUser().getStreamKey());
        model.addAttribute("product", broadcast.getProduct());
        return "broadcast";
    }
}
