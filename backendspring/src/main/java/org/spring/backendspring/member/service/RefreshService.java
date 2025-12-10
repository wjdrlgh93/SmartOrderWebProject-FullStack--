package org.spring.backendspring.member.service;

import org.spring.backendspring.member.dto.MemberDto;

public interface RefreshService {


    void addRefreshEntity(String userEmail, String refresh, long expiredMs);


    MemberDto validateRefreshTokenAndGetMember(String refresh);


    void deleteRefreshToken(String userEmail);
}
