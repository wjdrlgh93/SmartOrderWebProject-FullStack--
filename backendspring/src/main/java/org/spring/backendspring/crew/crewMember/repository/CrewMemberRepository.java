package org.spring.backendspring.crew.crewMember.repository;

import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMemberEntity, Long> {

    Optional<CrewMemberEntity> findByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    List<CrewMemberEntity> findByMemberEntity_id(Long memberId);

    Page<CrewMemberEntity> findAllByCrewEntityId(Long crewId, Pageable pageable);

    Page<CrewMemberEntity> findAllByCrewEntityIdAndMemberEntityId(Long crewId, Pageable pageable, Long searchId);

    Page<CrewMemberEntity> findAllByCrewEntityIdAndId(Long crewId, Pageable pageable, Long searchId);

    Page<CrewMemberEntity> findAllByCrewEntityIdAndRoleInCrew(Long crewId, Pageable pageable, CrewRole crewRole);

    List<CrewMemberEntity> findAllByCrewEntityId(Long crewId);
}
