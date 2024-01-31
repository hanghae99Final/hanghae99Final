package org.sparta.mytaek1.domain.user.service;

import org.sparta.mytaek1.domain.user.dto.JoinRequestDto;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void join(JoinRequestDto requestDto) {
        String userName = requestDto.getUserName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        User checkUsername = userRepository.findByUserName(userName).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (checkUsername != null) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        String userEmail = requestDto.getUserEmail();
        User checkEmail = userRepository.findByUserEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (checkEmail != null) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        User user = new User(userName, password, userEmail);
        userRepository.save(user);
    }
}
