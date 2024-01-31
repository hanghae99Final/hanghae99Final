package org.sparta.mytaek1.domain.user.dto;

import lombok.Getter;
import org.sparta.mytaek1.domain.user.entity.User;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String userName;
    private final String userEmail;
    private final String role;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.role = user.getRole().getAuthority();
    }
}
