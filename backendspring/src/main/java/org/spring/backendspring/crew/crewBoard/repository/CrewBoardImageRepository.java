package org.spring.backendspring.crew.crewBoard.repository;

import java.util.List;
import java.util.Optional;

import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBoardImageRepository extends JpaRepository<CrewBoardImageEntity, Long> {

    List<CrewBoardImageEntity> findByCrewBoardEntity(CrewBoardEntity crewBoardEntity);

    List<CrewBoardImageEntity> findByCrewBoardEntity_Id(Long id);

    Optional<CrewBoardImageEntity> findByNewName(String imageName);

    Optional<CrewBoardImageEntity> findByCrewBoardEntity_IdAndNewName(Long id, String imageName);
    
}
