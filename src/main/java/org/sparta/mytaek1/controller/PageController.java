package org.sparta.mytaek1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/broadcast")
    public String showBroadcast() {
        return "broadcast";
    }
}
