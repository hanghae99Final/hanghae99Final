package org.sparta.mytaek1.global.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "어디갔다가 왜 이제왔어 형..";
    }
}
