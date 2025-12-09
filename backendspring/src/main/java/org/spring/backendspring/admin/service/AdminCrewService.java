package org.spring.backendspring.admin.service;

import java.util.List;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;
import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;

public interface AdminCrewService {

    List<CrewDto> findAllCrew();

    CrewDto findCrew(Long crewId);

    void deleteCrewByAdmin(Long id);

    PagedResponse<CrewDto> findAllCrews(String keyword, int page, int size);

    PagedResponse<CrewCreateRequestDto> findAllCrewCreateRequest(String keyword, int page, int size);

    CrewCreateRequestDto crewRequestDetail(Long crewRequestId);

    CrewDto findByDetailCrew(Long crewId);
}
