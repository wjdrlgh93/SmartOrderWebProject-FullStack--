package org.spring.backendspring.crew.crewJoin.service;

import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrewJoinRequestService {
    CrewJoinRequestDto crewJoinRequest(CrewJoinRequestDto joinDto);

    Page<CrewJoinRequestDto> myCrewJoinList(Long crewId, Pageable pageable, String subject, String search);

    void crewJoinRequestApproved(CrewJoinRequestDto joinDto, Long leaderId);

    void crewJoinRequestRejected(CrewJoinRequestDto joinDto, Long leaderId);

}
