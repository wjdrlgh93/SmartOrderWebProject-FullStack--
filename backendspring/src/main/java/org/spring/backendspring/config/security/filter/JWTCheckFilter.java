package org.spring.backendspring.config.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.common.role.MemberRole;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.config.security.exception.CustomJWTException;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return request.getMethod().equals("OPTIONS")
                || path.startsWith("/api/member/login")
                || path.startsWith("/api/member/join")
                || path.startsWith("/index")
                || path.startsWith("/member")
                || path.startsWith("/api/board/index")
                || path.startsWith("/api/refresh/token");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        // Bearer -> 토큰 앞에 붙는 이름
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            // 토큰을 통해서 유저의 정보를 뜯음
            Map<String, Object> claims = jwtUtil.validateToken(token);
            Long memberId = ((Number) claims.get("id")).longValue();
            String userEmail = (String) claims.get("userEmail");
            String roleClaim = (String) claims.get("role");
            String normalized = roleClaim.startsWith("ROLE_") ? roleClaim.substring(5) : roleClaim;

            MemberEntity member = MemberEntity.builder()
                    .id(memberId)
                    .userEmail(userEmail)
                    .role(MemberRole.valueOf(normalized))
                    .build();

            MyUserDetails myUserDetails = new MyUserDetails(member);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            myUserDetails,
                            null,
                            myUserDetails.getAuthorities() // ROLE_붙인 권한 반환
                    );

            // 검증 통과 후 시큐리티에 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        } catch (CustomJWTException e) {
            log.error("JWT 에러 발생", e);
            if (e.getMessage().contains("Expired")) {
                sendErrorResponse(response, 401, "TOKEN_EXPIRED");
            } else {
                sendErrorResponse(response, 401, "INVALID_ACCESS_TOKEN");
            }
        } catch (Exception e) {
            log.error("응답 에러! 콘솔 확인 ㄱㄱ", e);
            filterChain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   int status,
                                   String body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String msg = new Gson().toJson(body);
        response.getWriter().write(msg);
    }
}
