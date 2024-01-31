package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/broadcasts")
@Slf4j
public class BroadcastController {

    private final BroadcastService broadcastService;


    @GetMapping
    public String getBroadcastsOnAir(Model model) {
        List<BroadcastResponseDto> broadcastResponseDtoList = broadcastService.getAllBroadCast();
        model.addAttribute("broadcastResponseDtoList", broadcastResponseDtoList);
        return "broadcastList";
    }

    @PostMapping
    public ResponseEntity createBroadcast(@RequestParam long userId,
                                          @RequestParam long productId,
                                          @RequestBody BroadcastRequestDto requestDto) {
        BroadcastResponseDto responseDto = broadcastService.createBroadcast(userId, productId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{broadcastId}/end")
    public ResponseEntity endBroadcast(@PathVariable long broadcastId) {
        BroadcastResponseDto responseDto = broadcastService.endBroadcast(broadcastId);
        return ResponseEntity.ok(responseDto);
    }
}
