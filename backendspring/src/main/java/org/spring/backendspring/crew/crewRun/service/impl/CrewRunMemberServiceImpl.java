package org.spring.backendspring.crew.crewRun.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.common.exception.CustomException;
import org.spring.backendspring.common.exception.ErrorCode;
import org.spring.backendspring.crew.crewRun.dto.CrewRunMemberDto;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.spring.backendspring.crew.crewRun.repository.CrewRunMemberRepository;
import org.spring.backendspring.crew.crewRun.repository.CrewRunRepository;
import org.spring.backendspring.crew.crewRun.service.CrewRunMemberService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewRunMemberServiceImpl implements CrewRunMemberService {
    private final CrewRunMemberRepository crewRunMemberRepository;
    private final CrewRunRepository crewRunRepository;
    private final MemberRepository memberRepository;

    @Override
    public Page<CrewRunMemberDto> findCrewRunMemberList(Long crewId, Long runId, Pageable pageable) {
        crewRunRepository.findByCrewEntityIdAndId(crewId, runId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_RUN_NOT_FOUND));

        Page<CrewRunMemberEntity> crewRunMemberList =
                crewRunMemberRepository.findAllByCrewRunEntityId(runId, pageable);

        return crewRunMemberList.map(CrewRunMemberDto::toCrewRunMemberDto);
    }

    @Override
    public void insertCrewMemberRun(Long runId, Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        CrewRunEntity crewRunEntity = crewRunRepository.findById(runId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_RUN_NOT_FOUND));

        Optional<CrewRunMemberEntity> opCrewRunMember =
                crewRunMemberRepository.findByCrewRunEntityIdAndMemberEntityId(runId, memberId);

        if (opCrewRunMember.isPresent()) {
            throw new CustomException(ErrorCode.CREW_RUN_MEMBER_ALREADY_EXISTS);
        }

        crewRunMemberRepository.save(
                CrewRunMemberEntity.insertCrewRun(
                        CrewRunMemberEntity.builder()
                                .crewRunEntity(crewRunEntity)
                                .memberEntity(memberEntity)
                                .build()
                )
        );
    }

    @Override
    public void deleteCrewMemberRun(Long runId, Long memberId) {
        crewRunMemberRepository.findByCrewRunEntityIdAndMemberEntityId(runId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_RUN_MEMBER_NOT_FOUND));

        crewRunMemberRepository.deleteByCrewRunEntityIdAndMemberEntityId(runId, memberId);
    }
}