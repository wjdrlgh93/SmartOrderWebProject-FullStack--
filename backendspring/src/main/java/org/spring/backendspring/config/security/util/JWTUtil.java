package org.spring.backendspring.config.security.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.config.security.exception.CustomJWTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JWTUtil {

    // 만료시간
    public static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 30;
    public static final long REFRESH_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000;

    // 임시 관리자 비밀키
    // yml에서 가져오는걸 추천
    public final SecretKey SECRET_KEY;

    public JWTUtil(@Value("${security.jwt.key}") String key) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String generateToken(Map<String, Object> claims, long min) {
        try {
            // JWT 파싱 및 검증
            return Jwts.builder()
                    .claim("id", claims.get("id"))
                    .claim("userEmail", claims.get("userEmail")) // 페이로드 설정 (사용자 정보)
                    .claim("role", claims.get("role"))
                    .claim("tokenType", claims.get("tokenType"))
                    .issuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발급 시각
                    .expiration(Date.from(ZonedDateTime.now().toInstant().plusMillis(min))) // 만료 시각
                    .signWith(SECRET_KEY) // 서명
                    .compact(); // 직렬화 (문자열 변환)
        } catch (JwtException e) {
            log.error("JWT 키 인코딩 오류 ", e);
            throw new RuntimeException(e);
        }
    }

    // accessToken 생성
    public String generateAccessToken(MemberDto member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("userEmail", member.getUserEmail());
        claims.put("role", member.getRole());
        claims.put("tokenType", "access");

        return generateToken(claims, ACCESS_EXPIRATION_TIME);
    }

    // refreshToken 생성
    public String generateRefreshToken(MemberDto member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userEmail", member.getUserEmail());
        claims.put("tokenType", "refresh");

        return generateToken(claims, REFRESH_EXPIRATION_TIME);
    }

    // 토큰 만료시간 검증
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build()
                .parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
    }

    // JWT 토큰 검증 및 클레임(사용자 정보) 반환
    public Map<String, Object> validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // JWT 예외처리
        } catch (ExpiredJwtException e) {
            throw new CustomJWTException("accessToken Expired"); // 만료된 토큰
        } catch (MalformedJwtException e) {
            throw new CustomJWTException("Malformed JWT"); // 잘못된 형식
        } catch (InvalidClaimException e) {
            throw new CustomJWTException("Invalid Claim"); // Claim 불일치
        } catch (JwtException e) {
            throw new CustomJWTException("Invalid JWT Signature or Structure"); // 기타
        } catch (Exception e) {
            throw new CustomJWTException("JWT Validation Error"); // 일반 예외
        }
    }

    // 토큰 타입 확인 (access, refresh)
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("tokenType", String.class);
    }

    // error 응답 json 변환
    // private void sendErrorResponse(HttpServletResponse response,
    //                                int status,
    //                                Map<String, String> body) throws IOException {
    //     response.setStatus(status);
    //     response.setContentType("application/json;charset=UTF-8");
    //     String msg = new Gson().toJson(body);
    //     response.getWriter().write(msg);
    // }

}
