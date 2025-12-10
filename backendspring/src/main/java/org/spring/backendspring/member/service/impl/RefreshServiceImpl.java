package org.spring.backendspring.member.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.common.exception.CustomException;
import org.spring.backendspring.common.exception.ErrorCode;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.RefreshEntity;
import org.spring.backendspring.member.repository.RefreshRepository;
import org.spring.backendspring.member.service.MemberService;
import org.spring.backendspring.member.service.RefreshService;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final RefreshRepository refreshRepository;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @Override
    public void addRefreshEntity(String userEmail, String refresh, long expiredMs) {
        Optional<RefreshEntity> refreshEntity = refreshRepository.findByUserEmail(userEmail);

        refreshEntity.ifPresent(entity -> {
                    entity.setUserEmail(userEmail);
                    entity.setRefresh(refresh);
                    entity.setExpiration(expiredMs);
                    refreshRepository.saveAndFlush(entity);
                });

        if (refreshEntity.isEmpty()) {
            refreshRepository.save(
                    RefreshEntity.builder()
                            .userEmail(userEmail)
                            .refresh(refresh)
                            .expiration(expiredMs)
                            .build()
            );
        }
    }

    @Override
    public MemberDto validateRefreshTokenAndGetMember(String refresh) {

        if (refresh == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISSING);
        }


        String tokenType = jwtUtil.getCategory(refresh);
        if (!tokenType.equals("refresh")) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }


        refreshRepository.findByRefresh(refresh)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));


        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }


        Map<String, Object> claims = jwtUtil.validateToken(refresh);
        String userEmail = (String) claims.get("userEmail");


        MemberDto memberDto = memberService.findByUserEmail(userEmail);
        if (memberDto == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return memberDto;
    }

    @Override
    public void deleteRefreshToken(String userEmail) {
        refreshRepository.findByUserEmail(userEmail)
                .ifPresent(entity -> refreshRepository.deleteById(entity.getId()));
    }
}
