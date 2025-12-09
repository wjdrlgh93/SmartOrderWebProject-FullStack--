package org.spring.backendspring.crew.crew.repository;

import java.util.Optional;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<CrewEntity, Long> {

    Optional<Object> findByIdAndCrewMemberEntities_MemberEntity_Id(Long crewId, Long loginUserId);

    Page<CrewEntity> findByNameContaining(String keyword, PageRequest pageRequest);

    Page<CrewEntity> findByDescriptionContaining(String keyword, PageRequest pageRequest);

    Page<CrewEntity> findByDistrictContaining(String keyword, PageRequest pageRequest);

    Page<CrewEntity> findByNameContainingOrDescriptionContainingOrDistrictContaining(String keyword, String keyword2,
            String keyword3, PageRequest pageRequest);

}
