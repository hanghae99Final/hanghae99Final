package org.sparta.mytaek1.domain.user.dto;

import lombok.Getter;

@Getter
public class JoinRequestDto {
    private String userName;
    private String userEmail;
    private String password;
}
