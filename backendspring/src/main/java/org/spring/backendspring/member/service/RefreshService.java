package org.spring.backendspring.member.service;

import org.spring.backendspring.member.dto.MemberDto;

public interface RefreshService {

    // 토큰 DB에 저장하기
    void addRefreshEntity(String userEmail, String refresh, long expiredMs);

    // refresh 토큰 유효성 검사 및 회원 추출
    MemberDto validateRefreshTokenAndGetMember(String refresh);

    // userEmail 확인 후 삭제
    void deleteRefreshToken(String userEmail);
}
