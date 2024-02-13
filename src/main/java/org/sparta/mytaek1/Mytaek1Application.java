package org.sparta.mytaek1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Mytaek1Application {

    public static void main(String[] args) {
        SpringApplication.run(Mytaek1Application.class, args);
    }

}
