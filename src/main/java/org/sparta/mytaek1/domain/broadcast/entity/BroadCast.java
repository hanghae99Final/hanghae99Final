package org.sparta.mytaek1.domain.broadcast.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BroadCast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long broadCastId;

    private String broadCastTitle;

    private String broadCastDescription;

    private boolean onAir;

//    private User user;

//    private Product product;

    //생성자 빌더는 나중에 다 풀받고

}
