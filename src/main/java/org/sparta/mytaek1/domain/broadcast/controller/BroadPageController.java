package org.sparta.mytaek1.domain.broadcast.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BroadPageController {
    @GetMapping("/broadcast")
    public String showBroadcast() {
        return "broadcast";
    }

    @GetMapping("/form")
    public String getBroadCastForm() {
        return "broadCastForm";
    }
}
