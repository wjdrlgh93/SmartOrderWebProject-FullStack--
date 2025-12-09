package org.spring.backendspring.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.member.dto.AuthResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.RefreshService;
import org.spring.backendspring.config.security.util.CookieUtil;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/refresh")
@RequiredArgsConstructor
public class RefreshController {

    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    @PostMapping("/token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    refresh = c.getValue();
                    break;
                }
            }
        }

        // refresh 토큰의 정보 검사 (만료, refresh 토큰 맞는지, user 정보 확인)
        MemberDto memberDto =
                refreshService.validateRefreshTokenAndGetMember(refresh);

        // 토큰 저장
        String newAccessToken = jwtUtil.generateAccessToken(memberDto);
        String newRefreshToken = jwtUtil.generateRefreshToken(memberDto);

        // uerEmail, refreshToken, 만료시간 저장 (토큰이 이미 있으면 삭제 후 저장)
        refreshService.addRefreshEntity(memberDto.getUserEmail(),
                newRefreshToken, JWTUtil.REFRESH_EXPIRATION_TIME);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(CookieUtil.createCookie("refreshToken", newRefreshToken));

        AuthResponse authResponse = AuthResponse.builder()
                .id(memberDto.getId())
                .accessToken(newAccessToken)
                .userEmail(memberDto.getUserEmail())
                .role(memberDto.getRole().toString())
                .build();

        log.info("---------------------------------");
        log.info("새로운 토큰 발급 완료: {}", newAccessToken);
        log.info("---------------------------------");
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
}
