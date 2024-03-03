package org.sparta.mytaek1.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sparta.mytaek1.global.message.ErrorMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String LOGIN_FAILED_ERROR_LOG = "로그인에 실패했습니다. 메시지 : {}";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(ErrorMessage.PASSWORD_MISMATCH_ERROR_MESSAGE.getErrorMessage());
        log.info(LOGIN_FAILED_ERROR_LOG, exception.getMessage());
    }
}

