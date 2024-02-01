package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BroadPageController {

    private final BroadcastService broadcastService;
    @GetMapping("/broadcast")
    public String showBroadcast() {
        return "broadcast";
    }

    @GetMapping("/form")
    public String getBroadCastForm() {
        return "broadcastForm";
    }

    @GetMapping
    public String getBroadcastsOnAir(Model model) {
        List<BroadcastResponseDto> broadcastResponseDtoList = broadcastService.getAllBroadCast();
        model.addAttribute("broadcastResponseDtoList", broadcastResponseDtoList);
        return "broadcastList";
    }
}
