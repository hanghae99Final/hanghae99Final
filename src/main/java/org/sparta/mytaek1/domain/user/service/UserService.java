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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserRequestDto requestDto) {
        String userName = requestDto.getUserName();
        checkDuplicatedUserName(userName);

        String userEmail = requestDto.getUserEmail();
        checkDuplicatedUserEmail(userEmail);

        String password = passwordEncoder.encode(requestDto.getPassword());
        User user =  userRepository.save(new User(userName, userEmail, password));
        new UserResponseDto(user);
    }

    private void checkDuplicatedUserName(String userName) {
        Optional<User> checkUsername = userRepository.findByUserName(userName);

        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_USER__ERROR_MESSAGE.getErrorMessage());
        }
    }

    private void checkDuplicatedUserEmail(String userEmail) {
        Optional<User> checkEmail = userRepository.findByUserEmail(userEmail);

        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_EMAIL_ERROR_MESSAGE.getErrorMessage());
        }
    }
}

