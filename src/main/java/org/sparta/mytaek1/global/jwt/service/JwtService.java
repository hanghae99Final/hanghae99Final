package org.sparta.mytaek1.global.jwt.service;

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
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer%20";
    private static final String REISSUE_ACCESS_TOKEN_LOG = "재발급된 Access Token : {}";
    private static final String TOKEN_HEADER_SET_LOG = "Access Token, Refresh Token 헤더 설정 완료";
    private static final String NOT_VALID_TOKEN_LOG = "유효하지 않은 토큰입니다. {}";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_AUTHORIZATION_HEADER = "Authorization";

    private final UserRepository userRepository;

    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info(REISSUE_ACCESS_TOKEN_LOG, accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        sendAccessTokenAsCookie(response, accessToken);
        sendRefreshTokenAsCookie(response, refreshToken);
        log.info(TOKEN_HEADER_SET_LOG);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(getAccessTokenFromRequest(request))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(EMAIL_CLAIM)
                .asString());
        } catch (Exception e) {
            log.error(ErrorMessage.INVALID_JWT_ERROR_MESSAGE.getErrorMessage());
            return Optional.empty();
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public void sendAccessTokenAsCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, BEARER + accessToken);
        cookie.setMaxAge(100000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void sendRefreshTokenAsCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("Authorization-refresh", BEARER + refreshToken);
        cookie.setMaxAge(1209600000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByUserEmail(email)
            .ifPresentOrElse(
                    user -> user.updateRefreshToken(refreshToken),
                    () -> new Exception(ErrorMessage.NOT_EXIST_USER_ERROR_MESSAGE.getErrorMessage())
            );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error(NOT_VALID_TOKEN_LOG, e.getMessage());
            return false;
        }
    }

    public String getAccessTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public String getRefreshTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}