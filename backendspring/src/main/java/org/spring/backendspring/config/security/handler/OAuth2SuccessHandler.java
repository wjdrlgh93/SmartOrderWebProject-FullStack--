package org.spring.backendspring.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.config.security.util.CookieUtil;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.spring.backendspring.member.dto.AuthResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.spring.backendspring.member.service.RefreshService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Log4j2
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final RefreshService refreshService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        String email = myUserDetails.getUsername();
        Long id = myUserDetails.getMemberId();
        String nickName = myUserDetails.getNickName();

        String role = myUserDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String normalized = role.startsWith("ROLE_") ? role.substring(5) : role;


        MemberDto memberDto = memberService.findByUserEmail(email);
        // jwt 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(memberDto);
        String refreshToken = jwtUtil.generateRefreshToken(memberDto);

        Cookie refreshCookie = CookieUtil.createCookie("refreshToken", refreshToken);
        response.addCookie(refreshCookie);

        refreshService.addRefreshEntity(email, refreshToken, JWTUtil.REFRESH_EXPIRATION_TIME);

        AuthResponse authResponse = AuthResponse.builder()
                .id(id)
                .accessToken(accessToken)
                .userEmail(email)
                .nickName(nickName)
                .role(normalized)
                .build();

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("http://localhost:3000/auth/oauth/success?token=" + accessToken);
        new ObjectMapper().writeValue(response.getWriter(), authResponse);

        log.info("---------------------------------------");
        log.info("OAuth2 토큰 발급 완료: {}", accessToken);
        log.info("---------------------------------------");
    }
}
