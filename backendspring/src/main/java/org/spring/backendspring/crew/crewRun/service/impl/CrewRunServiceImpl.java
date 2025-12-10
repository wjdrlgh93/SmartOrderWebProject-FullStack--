package org.spring.backendspring.crew.crewRun.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.common.exception.CustomException;
import org.spring.backendspring.common.exception.ErrorCode;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.crew.crewRun.dto.CrewRunDto;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.repository.CrewRunRepository;
import org.spring.backendspring.crew.crewRun.service.CrewRunService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewRunServiceImpl implements CrewRunService {
    private final CrewRunRepository crewRunRepository;
    private final MemberRepository memberRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;


    @Override
    public List<CrewRunDto> findCrewRunList(Long crewId) {
        return crewRunRepository.findAllByCrewEntityId(crewId)
                .stream()
                .map(CrewRunDto::toCrewRunDto)
                .collect(Collectors.toList());
    }

    @Override
    public CrewRunDto detailCrewRun(Long crewId, Long runId) {
        return crewRunRepository.findByCrewEntityIdAndId(crewId, runId)
                .map(CrewRunDto::toCrewRunDto)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_RUN_NOT_FOUND));
    }

    @Override
    public CrewRunDto crewRunCreate(CrewRunDto runDto) {
        Long crewId = runDto.getCrewId();
        Long memberId = runDto.getMemberId();

        LocalDateTime runToday = LocalDateTime.now();
        LocalDateTime startAt = runDto.getStartAt();
        LocalDateTime endAt   = runDto.getEndAt();


        if (startAt == null || endAt == null) {
            throw new CustomException(ErrorCode.RUN_TIME_REQUIRED);
        }

        if (startAt.isBefore(runToday)) {
            throw new CustomException(ErrorCode.RUN_START_BEFORE_NOW);
        }

        if (endAt.isBefore(runToday)) {
            throw new CustomException(ErrorCode.RUN_END_BEFORE_NOW);
        }

        if (!endAt.isAfter(startAt)) { // endAt <= startAt
            throw new CustomException(ErrorCode.RUN_END_NOT_AFTER_START);
        }



        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));


        crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_MEMBER_NOT_FOUND));

        CrewRunEntity crewRunEntity = crewRunRepository.save(
                CrewRunEntity.createCrewRun(
                        CrewRunDto.builder()
                                .title(runDto.getTitle())
                                .startAt(startAt)
                                .endAt(endAt)
                                .place(runDto.getPlace())
                                .routeHint(runDto.getRouteHint())
                                .crewEntity(crewEntity)
                                .memberEntity(memberEntity)
                                .build()
                )
        );

        return CrewRunDto.toCrewRunDto(crewRunEntity);
    }

    @Override
    public CrewRunDto crewRunUpdate(CrewRunDto runDto) {
        Long crewId = runDto.getCrewId();
        Long memberId = runDto.getMemberId();
        Long runId = runDto.getId();


        crewRunRepository.findById(runId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_RUN_NOT_FOUND));


        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));


        crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_MEMBER_NOT_FOUND));

        CrewRunEntity crewRunUpdateEntity = crewRunRepository.save(
                CrewRunEntity.updateCrewRun(
                        CrewRunDto.builder()
                                .id(runId)
                                .title(runDto.getTitle())
                                .startAt(runDto.getStartAt())
                                .endAt(runDto.getEndAt())
                                .place(runDto.getPlace())
                                .routeHint(runDto.getRouteHint())
                                .crewEntity(crewEntity)
                                .memberEntity(memberEntity)
                                .build()
                )
        );

        return CrewRunDto.toCrewRunDto(crewRunUpdateEntity);
    }

    @Override
    public void crewRunDelete(Long crewId, Long runId) {

        crewRunRepository.deleteByCrewEntityIdAndId(crewId, runId);
    }
}