package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.domain.user.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final BroadcastService broadcastService;

    @GetMapping("/broadcast/{broadcastId}")
    public String showBroadcast(@PathVariable Long broadcastId, Model model) {
        String streamKey = broadcastService.getStreamKeyByBroadcastId(broadcastId);
        model.addAttribute("streamKey", streamKey);
        return "broadcast";
    }
}
