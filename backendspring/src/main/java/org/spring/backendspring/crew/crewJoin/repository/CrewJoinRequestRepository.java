package org.spring.backendspring.crew.crewJoin.repository;

import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewJoinRequestRepository extends JpaRepository<CrewJoinRequestEntity, Long> {


    Optional<CrewJoinRequestEntity> findByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    Page<CrewJoinRequestEntity> findAllByCrewEntityId(Long crewId, Pageable pageable);

    void deleteByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);

    boolean existsByMemberEntity_Id(Long id);

    void deleteByMemberEntity_Id(Long id);


    Page<CrewJoinRequestEntity> findAllByCrewEntityIdAndStatus(Long crewId, Pageable pageable, RequestStatus status);

  
    Page<CrewJoinRequestEntity> findAllByCrewEntityIdAndId(Long crewId, Pageable pageable, Long searchId);

    Page<CrewJoinRequestEntity> findAllByCrewEntityIdAndMemberEntityId(Long crewId, Pageable pageable, Long searchId);

    Page<CrewJoinRequestEntity> findAllByCrewEntityIdAndMessageContaining(Long crewId, Pageable pageable,
            String search);

}
