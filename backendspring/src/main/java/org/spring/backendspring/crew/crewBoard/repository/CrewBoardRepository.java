package org.spring.backendspring.crew.crewBoard.repository;

import java.util.Optional;

import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBoardRepository extends JpaRepository<CrewBoardEntity, Long> {

    Page<CrewBoardEntity> findByCrewEntity_Id(Long crewId, Pageable pageable);

    Optional<CrewBoardEntity> findByCrewEntity_IdAndId(Long crewId, Long id);

    Page<CrewBoardEntity> findByTitleContaining(String keyword, PageRequest pageRequest);

    Page<CrewBoardEntity> findByCrewEntity_IdAndTitleContaining(Long crewId, String keyword, PageRequest pageRequest);

    Page<CrewBoardEntity> findByCrewEntity_IdAndContentContaining(Long crewId, String keyword, PageRequest pageRequest);

    Page<CrewBoardEntity> findByCrewEntity_IdAndTitleContainingOrContentContainingOrMemberEntity_NickNameContaining(
            Long crewId, String keyword, String keyword2,String keyword3, PageRequest pageRequest);

    Page<CrewBoardEntity> findByCrewEntity_IdAndMemberEntity_NickNameContaining(Long crewId, String keyword,
            PageRequest pageRequest);
    
}
