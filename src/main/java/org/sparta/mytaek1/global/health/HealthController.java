package org.sparta.mytaek1.global.health;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "전 건강해요~!!";
    }
}
