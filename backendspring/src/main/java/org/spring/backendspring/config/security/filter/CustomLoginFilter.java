package org.spring.backendspring.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.config.security.exception.EmptyCredentialsException;
import org.spring.backendspring.config.security.util.CookieUtil;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.spring.backendspring.member.dto.AuthResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.RefreshService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 아이디와 비밀번호 기반의 데이터를 Form 데이터로 전송을 받아 '인증'을 담당하는 필터 역할의 클래스입니다.
@Log4j2
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    // 로그인 userEmail, userPw 가져옵니다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        String userEmail = obtainUsername(request);
        String userPw = obtainPassword(request);

        if (userEmail.trim().isEmpty() || userPw.trim().isEmpty()) {
            throw new EmptyCredentialsException("로그인 형식이 올바르지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userEmail, userPw);

        try {
            return authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            System.out.println("authenticate()에서 예외 발생: " + e);
            throw e;
        }
    }

    // 성공시 accessToken, refreshToken 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        log.info("---------------------------------------");
        log.info("로그인 성공: {}", authResult.getPrincipal());
        log.info("---------------------------------------");

        MyUserDetails myUserDetails = (MyUserDetails) authResult.getPrincipal();
        String userEmail = myUserDetails.getUsername();
        Long id = myUserDetails.getMemberId();
        String nickName = myUserDetails.getNickName();
        System.out.println(id + " <<< memberId");

        String role = authResult.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String normalized = role.startsWith("ROLE_") ? role.substring(5) : role;

        MemberDto member = MemberDto.builder()
                .id(id)
                .userEmail(userEmail)
                .role(MemberRole.valueOf(normalized))
                .build();

        String accessToken = jwtUtil.generateAccessToken(member);
        String refreshToken = jwtUtil.generateRefreshToken(member);

        // refresh DB 저장
        refreshService.addRefreshEntity(userEmail, refreshToken, JWTUtil.REFRESH_EXPIRATION_TIME);

        // refreshToken을 httpOnly 쿠키에 담는다.
        Cookie refreshCookie = CookieUtil.createCookie("refreshToken", refreshToken);
        response.addCookie(refreshCookie);

        // 응답 DTO에 저장
        AuthResponse authResponse = AuthResponse.builder()
                .id(id)
                .accessToken(accessToken)
                .userEmail(userEmail)
                .nickName(nickName)
                .role(normalized)
                .build();

        // AccessToken은 헤더에 담아서 프론트로 전송
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), authResponse);

        log.info("---------------------------------------");
        log.info("토큰 발급 완료: {}", accessToken);
        log.info("refresh 토큰 발급 완료: {}", refreshToken);
        log.info("---------------------------------------");
    }

    // 인증 실패 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
