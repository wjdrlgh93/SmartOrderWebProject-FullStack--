package org.spring.backendspring.member.service;

import org.spring.backendspring.member.dto.MemberProfileImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberProfileImageService {

    MemberProfileImageDto findByMemberEntity_id(Long memberId);

    void uploadProfileImage(MultipartFile file, Long memberId);
}
