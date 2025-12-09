package org.spring.backendspring.crew.crewMember.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.common.exception.CustomException;
import org.spring.backendspring.common.exception.ErrorCode;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewMemberServiceImpl implements CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;
    private final MemberRepository memberRepository;
    
    @Override
    public Page<CrewMemberDto> findCrewMemberList(Long crewId, Pageable pageable, String subject, String search) {
        Page<CrewMemberEntity> crewMemberList;

        if (subject == null || search == null || search.equals("")) {
            crewMemberList = crewMemberRepository.findAllByCrewEntityId(crewId, pageable);
        } else if ("id".equals(subject)) {
            Long searchId = Long.parseLong(search);
            crewMemberList = crewMemberRepository
                    .findAllByCrewEntityIdAndId(crewId, pageable, searchId);
        } else if ("memberId".equals(subject)) {
            Long searchId = Long.parseLong(search);
            crewMemberList = crewMemberRepository
                    .findAllByCrewEntityIdAndMemberEntityId(crewId, pageable, searchId);
        } else if ("status".equals(subject)) {
            CrewRole crewRole = CrewRole.valueOf(search);
            crewMemberList = crewMemberRepository
                    .findAllByCrewEntityIdAndRoleInCrew(crewId, pageable, crewRole);
        } else {
            crewMemberList = crewMemberRepository.findAllByCrewEntityId(crewId, pageable);
        }

        return crewMemberList.map(CrewMemberDto::toCrewMember);
    }

    @Override
    public void deleteCrewMember(Long crewMemberTbId) {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(crewMemberTbId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_MEMBER_NOT_FOUND));
        crewMemberRepository.delete(crewMemberEntity);
    }

    @Override
    public CrewMemberDto detailCrewMember(Long crewId, Long crewMemberId) {
        return crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, crewMemberId)
                .map(CrewMemberDto::toCrewMember)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_MEMBER_NOT_FOUND));
    }

    @Override
    public List<CrewMemberDto> myCrewList(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        
        List<CrewMemberEntity> mycrewList = member.getCrewMemberEntityList();

        return mycrewList.stream()
                    .map(crew -> CrewMemberDto.toCrewMember(crew))
                    .toList();
    }
    

}