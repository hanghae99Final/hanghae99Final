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
    private static final String CHARACTER_ENCODE = "UTF-8";
    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(CHARACTER_ENCODE);
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(ErrorMessage.PASSWORD_MISMATCH_ERROR_MESSAGE.getErrorMessage());
        log.info(LOGIN_FAILED_ERROR_LOG, exception.getMessage());
    }
}

