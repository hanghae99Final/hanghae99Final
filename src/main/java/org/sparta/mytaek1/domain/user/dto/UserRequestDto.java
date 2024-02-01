package org.sparta.mytaek1.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRequestDto {
    private String userName;
    private String userEmail;
    private String password;
    private String userPhone;
    private String userAddress;
    private String postcode;

    public UserRequestDto(String userName, String userEmail, String password, String userPhone, String userAddress, String postcode) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.postcode = postcode;
    }
}
