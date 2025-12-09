package org.spring.backendspring.crew.crewCreate.service;

import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;

public interface CrewCreateRequestService {

    void createRequest(CrewCreateRequestDto crewCreateRequestDto, Long loginUserId);

    void rejectRequest(Long requestId);

    void approveRequest(Long requestId);
    
}
