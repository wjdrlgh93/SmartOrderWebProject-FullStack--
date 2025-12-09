package org.spring.backendspring.crew.crew.repository;

import java.util.List;
import java.util.Optional;

import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewImageRepository extends JpaRepository<CrewImageEntity, Long> {

    List<CrewImageEntity> findByCrewEntity_Id(Long id);

    Optional<CrewImageEntity> findByNewName(String imageName);

    List<CrewImageEntity> findByCrewEntity(CrewEntity crew);
    
}
