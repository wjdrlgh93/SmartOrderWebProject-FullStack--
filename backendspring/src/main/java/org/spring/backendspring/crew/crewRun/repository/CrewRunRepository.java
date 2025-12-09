package org.spring.backendspring.crew.crewRun.repository;

import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrewRunRepository extends JpaRepository<CrewRunEntity,Long> {
    List<CrewRunEntity> findAllByCrewEntityId(Long crewId);


    Optional<CrewRunEntity> findByCrewEntityIdAndId(Long crewId, Long runId);


    void deleteByCrewEntityIdAndId(Long crewId, Long runId);

    Optional<CrewRunEntity> findByCrewEntityIdAndMemberEntityId(Long crewId, Long memberId);


    List<CrewRunEntity> findByCreateTimeBetween(LocalDateTime dateStart, LocalDateTime dateEnd);


    List<CrewRunEntity> findByStartAtBetween(LocalDateTime dateStart, LocalDateTime dateEnd);


    List<CrewRunEntity> findByCrewEntityIdAndStartAtBetween(Long crewId, LocalDateTime dateStart, LocalDateTime dateEnd);

   


}
