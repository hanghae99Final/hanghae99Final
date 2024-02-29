package org.sparta.mytaek1.global.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.domain.user.dto.LoginRequestDto;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.sparta.mytaek1.global.message.SuccessMessage;
import org.sparta.mytaek1.global.security.UserDetailsImpl;
import org.sparta.mytaek1.global.security.UserRoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String LOGIN_ATTEMPT_LOG = "로그인 시도";
    private static final String LOGIN_SUCCESS_AND_JWT_TOKEN_CREATION_LOG = "로그인 성공 및 JWT 생성";
    private static final String LOGIN_FAIL_LOG = "로그인 실패";
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(LOGIN_ATTEMPT_LOG);
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info(LOGIN_SUCCESS_AND_JWT_TOKEN_CREATION_LOG);

        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum auth = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createToken(email, auth);
        String refreshToken = jwtUtil.createRefreshToken(email, auth);

        jwtUtil.addJwtToCookie(accessToken, response);
        jwtUtil.addRefreshTokenToCookie(refreshToken, response);

        response.getWriter().write(SuccessMessage.LOGIN_SUCCESS_MESSAGE.getSuccessMessage());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(LOGIN_FAIL_LOG);
        response.setStatus(401);
        response.getWriter().write(ErrorMessage.PASSWORD_MISMATCH_ERROR_MESSAGE.getErrorMessage());
    }
}