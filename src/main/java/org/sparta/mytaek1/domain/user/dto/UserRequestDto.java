package org.sparta.mytaek1.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRequestDto {

    private String userName;
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성되어야 합니다.")
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
