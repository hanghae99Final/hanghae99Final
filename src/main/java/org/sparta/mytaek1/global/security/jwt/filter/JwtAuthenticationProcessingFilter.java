package org.sparta.mytaek1.global.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.user.entity.User;
import org.sparta.mytaek1.domain.user.repository.UserRepository;
import org.sparta.mytaek1.global.security.jwt.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String CHECK_ACCESS_LOG_MESSAGE = "checkAccessTokenAndAuthentication() 호출";
    private static final String NO_CHECK_URL = "/login";
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken == null && accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken != null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
            log.info("AccessToken 발견");
        }
        if (accessToken == null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken, request,filterChain);
            log.info("AccessToken 없음");
        }
    }
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken, HttpServletRequest request, FilterChain filterChain) throws ServletException, IOException{

        User user = userRepository.findByRefreshToken("Bearer##" + refreshToken).orElseThrow();
        String newAccessToken = jwtService.createAccessToken(user.getUserEmail());
        jwtService.sendAccessToken(response, newAccessToken);

        String realToken = jwtService.substringToken(newAccessToken);
        if (realToken != null && jwtService.isTokenValid(realToken)) {
            jwtService.extractEmail(realToken)
                    .ifPresent(email -> userRepository.findByUserEmail(email)
                            .ifPresent(this::saveAuthentication));
        }

        filterChain.doFilter(request, response);
    }


    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        accessToken = jwtService.substringToken(accessToken);
        if (accessToken != null && jwtService.isTokenValid(accessToken)) {
            jwtService.extractEmail(accessToken)
                    .ifPresent(email -> userRepository.findByUserEmail(email)
                            .ifPresent(this::saveAuthentication));
        }

        filterChain.doFilter(request, response);
    }

    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    public void saveAuthentication(User myMember) {
        String password = myMember.getPassword();

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myMember.getUserEmail())
                .password(password)
                .roles(myMember.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}