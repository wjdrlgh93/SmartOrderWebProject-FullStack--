package org.spring.backendspring.crew.crew.service;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.springframework.web.multipart.MultipartFile;

public interface CrewService {

    CrewDto updateCrew(Long loginUserId, Long crewId, CrewDto crewDto, 
                       List<MultipartFile> newImages, List<String> deleteImageName) throws IOException;

    void deleteCrew(Long crewId, Long loginUserId);

    PagedResponse<CrewDto> crewList(String subject, String keyword, int page, int size);

    CrewDto crewDetail(Long crewId);

    CrewDto findMyCrew(Long crewId, Long memberId);

    List<CrewDto> findAllCrew();

    List<CrewMemberDto> findMyAllCrew(Long memberId);

    List<CrewDto> myCrewList(Long memberId);
}