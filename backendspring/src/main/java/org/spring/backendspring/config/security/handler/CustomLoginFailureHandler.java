package org.spring.backendspring.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.spring.backendspring.config.security.exception.EmptyCredentialsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "로그인 형식이 올바르지 않습니다.";
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (exception instanceof EmptyCredentialsException) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorMessage = "로그인 형식이 올바르지 않습니다.";
        } else if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            errorMessage = "이메일 또는 비밀번호가 맞지 않습니다.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "이메일 또는 비밀번호가 맞지 않습니다.";
        } else {
            errorMessage = "로그인 처리 중 알 수 없는 오류가 발생했습니다.";
        }

        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(),
                Map.of("status", response.getStatus(),
                        "error", errorMessage));
    }
}
