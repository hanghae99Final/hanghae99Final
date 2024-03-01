package org.sparta.mytaek1.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_EXIST_EMAIL_ERROR_MESSAGE.getErrorMessage()));

        return new org.springframework.security.core.userdetails.User(
            user.getUserEmail(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}