package org.spring.backendspring.crew.crewJoin.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.common.exception.CustomException;
import org.spring.backendspring.common.exception.ErrorCode;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.crew.CrewRoleCheck;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.crew.crewJoin.repository.CrewJoinRequestRepository;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewJoinRequestServiceImpl implements CrewJoinRequestService {

    private final CrewJoinRequestRepository crewJoinRequestRepository;
    private final MemberRepository memberRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewRepository crewRepository;

    @Override
    public CrewJoinRequestDto crewJoinRequest(CrewJoinRequestDto joinDto) {
        Long crewId = joinDto.getCrewRequestId();
        Long memberId = joinDto.getMemberRequestId();


        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_NOT_FOUND));


        Optional<CrewMemberEntity> optionalCrewMember =
                crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);

        if (optionalCrewMember.isPresent()) {
            throw new CustomException(ErrorCode.CREW_JOIN_ALREADY_MEMBER);
        }


        Optional<CrewJoinRequestEntity> optionalCrewJoinRequestEntity =
                crewJoinRequestRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId);

        if (optionalCrewJoinRequestEntity.isPresent()) {
            throw new CustomException(ErrorCode.CREW_JOIN_REQUEST_ALREADY_EXISTS);
        }

        CrewJoinRequestEntity crewJoinRequestEntity =
                crewJoinRequestRepository.save(
                        CrewJoinRequestEntity.insertCrewJoinRequest(
                                CrewJoinRequestDto.builder()
                                        .crewEntity(crewEntity)
                                        .memberEntity(memberEntity)
                                        .message(joinDto.getMessage())
                                        .build()
                        ));

        return CrewJoinRequestDto.crewJoinRequestDto(crewJoinRequestEntity);
    }

    @Override
    public Page<CrewJoinRequestDto> myCrewJoinList(Long crewId, Pageable pageable, String subject, String search) {
        Page<CrewJoinRequestEntity> crewJoinRequestList;

        if (subject == null || search == null || search.equals("")) {
            crewJoinRequestList = crewJoinRequestRepository.findAllByCrewEntityId(crewId, pageable);
        } else if ("status".equals(subject)) {
            RequestStatus status = RequestStatus.valueOf(search);
            crewJoinRequestList = crewJoinRequestRepository
                    .findAllByCrewEntityIdAndStatus(crewId, pageable, status);

        } else if ("id".equals(subject)) {
            Long searchId = Long.parseLong(search);
            crewJoinRequestList = crewJoinRequestRepository
                    .findAllByCrewEntityIdAndId(crewId, pageable, searchId);

        } else if ("memberRequestId".equals(subject)) {
            Long searchId = Long.parseLong(search);
            crewJoinRequestList = crewJoinRequestRepository
                    .findAllByCrewEntityIdAndMemberEntityId(crewId, pageable, searchId);

        } else if ("message".equals(subject)) {
            crewJoinRequestList = crewJoinRequestRepository
                    .findAllByCrewEntityIdAndMessageContaining(crewId, pageable, search);

        } else {
            crewJoinRequestList = crewJoinRequestRepository.findAllByCrewEntityId(crewId, pageable);
        }

        return crewJoinRequestList.map(CrewJoinRequestDto::crewJoinRequestDto);
    }


    @Override
    public void crewJoinRequestApproved(CrewJoinRequestDto joinDto, Long leaderId) {
        Long crewId = joinDto.getCrewRequestId();
        Long memberId = joinDto.getMemberRequestId();


        String crewRole = CrewRoleCheck.crewRoleCheckFn(leaderId, crewId, crewRepository);

        if (!"LEADER".equals(crewRole)) {
            throw new CustomException(ErrorCode.CREW_PERMISSION_DENIED);
        }

        CrewJoinRequestEntity request = crewJoinRequestRepository
                .findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_JOIN_REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new CustomException(ErrorCode.CREW_JOIN_REQUEST_ALREADY_PROCESSED);
        }


        crewJoinRequestRepository.save(CrewJoinRequestEntity.updateCrewJoinApproved(request));


        crewMemberRepository.save(CrewMemberEntity.insertCrewMember(request));
    }


    @Override
    public void crewJoinRequestRejected(CrewJoinRequestDto joinDto, Long leaderId) {
        Long crewId = joinDto.getCrewRequestId();
        Long memberId = joinDto.getMemberRequestId();

        String crewRole = CrewRoleCheck.crewRoleCheckFn(leaderId, crewId, crewRepository);

        if (!"LEADER".equals(crewRole)) {
            throw new CustomException(ErrorCode.CREW_PERMISSION_DENIED);
        }

        CrewJoinRequestEntity request = crewJoinRequestRepository
                .findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREW_JOIN_REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new CustomException(ErrorCode.CREW_JOIN_REQUEST_ALREADY_PROCESSED);
        }


        crewJoinRequestRepository.save(CrewJoinRequestEntity.updateCrewJoinRejected(request));
    }

}