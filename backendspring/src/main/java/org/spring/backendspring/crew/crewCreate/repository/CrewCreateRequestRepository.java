package org.spring.backendspring.crew.crewCreate.repository;

import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewCreateRequestRepository extends JpaRepository<CrewCreateRequestEntity, Long> {

    boolean existsByMemberEntity_Id(Long id);

    void deleteByMemberEntity_Id(Long id);

    Page<CrewCreateRequestEntity> findByCrewNameContaining(String keyword, Pageable pageable);
}
