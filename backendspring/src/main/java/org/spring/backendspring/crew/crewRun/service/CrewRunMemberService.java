package org.spring.backendspring.crew.crewRun.service;


import org.spring.backendspring.crew.crewRun.dto.CrewRunMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrewRunMemberService {

    void insertCrewMemberRun(Long runId, Long memberId);

    void deleteCrewMemberRun(Long runId, Long memberId);

    Page<CrewRunMemberDto> findCrewRunMemberList(Long crewId, Long runId, Pageable pageable);
}
