package org.sparta.mytaek1.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.user.dto.UserRequestDto;
import org.sparta.mytaek1.domain.user.dto.UserResponseDto;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserRequestDto requestDto) {
        String userName = requestDto.getUserName();
        String userEmail = requestDto.getUserEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String streamKey = UUID.randomUUID() + userEmail.split("@")[0];
        String userPhone = requestDto.getUserPhone();
        String userAddress = requestDto.getUserAddress();
        String postcode = requestDto.getPostcode();

        User user =  userRepository.save(new User(userName, userEmail, password, streamKey, userPhone, userAddress, postcode));
        new UserResponseDto(user);
    }

    public void checkStreamKey(String streamKey) {
        Boolean check = userRepository.existsByStreamKey(streamKey);
        if(!check){
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_STREAMKEY_ERROR_MESSAGE.getErrorMessage());
        }
    }
}

