package org.sparta.mytaek1.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.user.dto.UserRequestDto;
import org.sparta.mytaek1.domain.user.service.UserService;
import org.sparta.mytaek1.global.message.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        try {
            userService.createUser(requestDto);
            return new ResponseEntity<>(SuccessMessage.JOIN_SUCCESS_MESSAGE.getSuccessMessage(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stream-keys/{streamKey}")
    public ResponseEntity<String> checkStreamKey(@PathVariable String streamKey){
        userService.checkStreamKey(streamKey);
        return ResponseEntity.ok(SuccessMessage.VALID_STREAM_KEY_MESSAGE.getSuccessMessage());
    }
}