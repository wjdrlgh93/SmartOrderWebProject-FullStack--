package org.spring.backendspring.member.repository;

import java.util.List;
import java.util.Optional;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUserEmail(String userEmail);

    @EntityGraph(attributePaths = { "crewEntityList", "crewMemberEntityList" })
    
    Optional<MemberEntity> findById(Long id);
}
