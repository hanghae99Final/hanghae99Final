package org.sparta.mytaek1.domain.broadcast.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastRequestDto;
import org.sparta.mytaek1.domain.broadcast.dto.BroadcastResponseDto;
import org.sparta.mytaek1.domain.broadcast.dto.testRequestDto;
import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.sparta.mytaek1.domain.broadcast.service.BroadcastService;
import org.sparta.mytaek1.global.message.SuccessMessage;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


//    @PostMapping
//    public ResponseEntity createBroadcast(@RequestParam long userId,
//                                          @RequestParam long productId,
//                                          @RequestBody BroadcastRequestDto requestDto) {
//        BroadcastResponseDto responseDto = broadcastService.createBroadcast(userId, productId, requestDto);
//        return ResponseEntity.ok(responseDto);
//    }

    @PostMapping
    public ResponseEntity<String> createTest(@AuthenticationPrincipal UserDetailsImpl auth,
                                             @ModelAttribute testRequestDto requestDto) {
        BroadcastResponseDto responseDto = broadcastService.createTest(auth, requestDto);
        return new ResponseEntity<>(SuccessMessage.BROADCAST_START_MESSAGE.getSuccessMessage(), HttpStatus.CREATED);
    }

    @PostMapping("/{broadcastId}/end")
    public ResponseEntity endBroadcast(@PathVariable long broadcastId) {
        BroadcastResponseDto responseDto = broadcastService.endBroadcast(broadcastId);
        return new ResponseEntity<>(SuccessMessage.BROADCAST_END_MESSAGE.getSuccessMessage(), HttpStatus.OK);
    }
}
