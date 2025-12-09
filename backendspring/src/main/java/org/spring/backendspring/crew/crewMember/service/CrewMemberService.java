package org.spring.backendspring.crew.crewMember.service;

import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrewMemberService {
    
    void deleteCrewMember(Long crewMemberTbId);

    CrewMemberDto detailCrewMember(Long crewId, Long crewMemberId);

    Page<CrewMemberDto> findCrewMemberList(Long crewId, Pageable pageable, String subject, String search);

    List<CrewMemberDto> myCrewList(Long memberId);
}
