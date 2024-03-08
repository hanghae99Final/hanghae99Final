package org.sparta.mytaek1.global.security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        org.sparta.mytaek1.domain.user.entity.User user = userRepository.findByUserEmail(email).orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_EXIST_EMAIL_ERROR_MESSAGE.getErrorMessage()));

        return User.builder()
                .username(user.getUserEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}