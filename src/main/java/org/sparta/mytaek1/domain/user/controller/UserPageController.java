package org.sparta.mytaek1.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    @GetMapping("/my-page")
    public String myPage(Model model) {
        model.addAttribute("userName", "userName");
        model.addAttribute("userEmail", "userEmail");
        return "my_page";
    }

    @GetMapping("/api/user/login-page")
    public String signPage() {
        return "sign";
    }
}
