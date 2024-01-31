package org.sparta.mytaek1.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserPageController {

    @GetMapping("/mypage")
    public String home(Model model) {
        model.addAttribute("userName", "userName");
        model.addAttribute("userEmail", "userEmail");
        return "mypage";
    }

    @GetMapping("/sign")
    public String signupPage() {
        return "sign";
    }
}
