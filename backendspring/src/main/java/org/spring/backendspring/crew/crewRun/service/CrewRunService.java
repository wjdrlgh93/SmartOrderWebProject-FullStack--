package org.spring.backendspring.crew.crewRun.service;


import org.spring.backendspring.crew.crewRun.dto.CrewRunDto;

import java.util.List;

public interface CrewRunService {
    List<CrewRunDto> findCrewRunList(Long crewId);

    CrewRunDto detailCrewRun(Long crewId, Long runId);

    CrewRunDto crewRunCreate(CrewRunDto runDto);

    void crewRunDelete(Long crewId, Long runId);

    CrewRunDto crewRunUpdate(CrewRunDto runDto);
}
