package org.sparta.mytaek1.global.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private int accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private int refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer##";

    private final UserRepository userRepository;

    //jwt 생성
    public String createAccessToken(String email) {
        Date now = new Date();
        String token = JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
        return BEARER + token;
    }

    //refreshToken 생성
    public String createRefreshToken() {
        Date now = new Date();
        String token =  JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));

        return BEARER + token;
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setMaxAge(accessTokenExpirationPeriod);
        response.addCookie(accessCookie);
        log.info("Access Token 쿠키 설정 완료");
    }

        public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
            response.setStatus(HttpServletResponse.SC_OK);
            Cookie accessCookie = new Cookie("access_token", accessToken);
            accessCookie.setMaxAge(accessTokenExpirationPeriod);
            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setMaxAge(refreshTokenExpirationPeriod);
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);
            log.info("Access Token, Refresh Token 쿠키 설정 완료");
        }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && token.startsWith(BEARER)) {
                        return Optional.of(token.replace(BEARER, ""));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && token.startsWith(BEARER)) {
                        return Optional.of(token.replace(BEARER, ""));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByUserEmail(email)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            log.info(token);
            return true;
        } catch (Exception e) {
            log.info(token);
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER)) {
            return tokenValue.substring(8);
        }
        log.error(ErrorMessage.NOT_EXIST_TOKEN_ERROR_MESSAGE.getErrorMessage());
        throw new NullPointerException(ErrorMessage.NOT_EXIST_TOKEN_ERROR_MESSAGE.getErrorMessage());
    }
}

