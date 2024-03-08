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
        User user =  userRepository.save(
            new User(
                    requestDto.getUserName(),
                    requestDto.getUserEmail(),
                    passwordEncoder.encode(requestDto.getPassword()),
                    UUID.randomUUID() + requestDto.getUserEmail().split("@")[0],
                    requestDto.getUserPhone(),
                    requestDto.getUserAddress(),
                    requestDto.getPostcode()
            )
        );

        new UserResponseDto(user);
    }

    public void checkStreamKey(String streamKey) {
        if (!userRepository.existsByStreamKey(streamKey)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_STREAM_KEY_ERROR_MESSAGE.getErrorMessage());
        }
    }

    public User findByUserEmail(String email) {
        return userRepository.findByUserEmail(email).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.NOT_EXIST_USER_ERROR_MESSAGE.getErrorMessage()));
    }
}

