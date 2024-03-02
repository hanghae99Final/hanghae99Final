package org.sparta.mytaek1.global.security.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.security.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String LOGIN_SUCCESS_EMAIL_LOG = "로그인에 성공하였습니다. 이메일 : {}";
    private static final String LOGIN_SUCCESS_ACCESS_TOKEN_LOG = "로그인에 성공하였습니다. AccessToken : {}";
    private static final String LOGIN_SUCCESS_TOKEN_EXPIRED_LOG = "발급된 AccessToken 만료 기간 : {}";
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = extractUsername(authentication);
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        userRepository.findByUserEmail(email).ifPresent(user -> {
            user.updateRefreshToken(refreshToken);
            userRepository.saveAndFlush(user);
        });

        log.info(LOGIN_SUCCESS_EMAIL_LOG, email);
        log.info(LOGIN_SUCCESS_ACCESS_TOKEN_LOG, accessToken);
        log.info(LOGIN_SUCCESS_TOKEN_EXPIRED_LOG, accessTokenExpiration);
        log.info(authentication.getPrincipal().toString());
        log.info(authentication.getAuthorities().toString());
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
