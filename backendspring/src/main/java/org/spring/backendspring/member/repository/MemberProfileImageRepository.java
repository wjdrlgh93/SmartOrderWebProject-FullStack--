package org.spring.backendspring.member.repository;

import java.util.Optional;

import org.spring.backendspring.member.entity.MemberProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImageEntity, Long> {
    Optional<MemberProfileImageEntity> findByMemberEntity_id(Long memberId);
    
}
