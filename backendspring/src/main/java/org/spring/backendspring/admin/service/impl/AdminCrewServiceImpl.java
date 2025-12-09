package org.spring.backendspring.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.admin.repository.AdminCrewRepository;
import org.spring.backendspring.admin.service.AdminCrewService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;
import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;
import org.spring.backendspring.crew.crewCreate.repository.CrewCreateRequestRepository;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCrewServiceImpl implements AdminCrewService {

    private final CrewRepository crewRepository;
    private final CrewCreateRequestRepository crewCreateRequestRepository;
    private final AdminCrewRepository adminCrewRepository;

    @Override
    public List<CrewDto> findAllCrew() {
        return crewRepository.findAll().stream().map(CrewDto::toCrewDto).collect(Collectors.toList());
    }

    @Override
    public CrewDto findCrew(Long crewId) {
        return adminCrewRepository.findById(crewId)
                .map(CrewDto::toCrewDto)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
    }

    @Override
    public PagedResponse<CrewDto> findAllCrews(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CrewEntity> crewPage = adminCrewRepository.findAll(pageable);

        if (crewPage.isEmpty()) {
            throw new IllegalArgumentException("크루가 없습니다.");
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            crewPage = adminCrewRepository.findAll(pageable);
        } else {
            crewPage = adminCrewRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        return PagedResponse.of(crewPage.map(CrewDto::toCrewDto));
    }

    @Override
    public PagedResponse<CrewCreateRequestDto> findAllCrewCreateRequest(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CrewCreateRequestEntity> crewCreateRequestAll = crewCreateRequestRepository.findAll(pageable);

        if (crewCreateRequestAll.isEmpty()) {
            throw new IllegalArgumentException("크루 신청 요청이 없습니다!");
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            crewCreateRequestAll = crewCreateRequestRepository.findAll(pageable);
        } else {
            crewCreateRequestAll = crewCreateRequestRepository.findByCrewNameContaining(keyword, pageable);
        }

        return PagedResponse.of(crewCreateRequestAll.map(CrewCreateRequestDto::toDto));
    }

    @Override
    public CrewCreateRequestDto crewRequestDetail(Long crewRequestId) {
        CrewCreateRequestEntity crewCreateRequestEntity = crewCreateRequestRepository.findById(crewRequestId)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
        return CrewCreateRequestDto.toDto(crewCreateRequestEntity);
    }

    @Override
    public CrewDto findByDetailCrew(Long crewId) {
        CrewEntity crewEntity = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("없는 크루입니다."));
        return CrewDto.toCrewDto(crewEntity);
    }

    @Override
    public void deleteCrewByAdmin(Long id) {
        CrewEntity crew = crewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("크루를 찾을 수 없습니다."));
        crewRepository.delete(crew);
    }

}
