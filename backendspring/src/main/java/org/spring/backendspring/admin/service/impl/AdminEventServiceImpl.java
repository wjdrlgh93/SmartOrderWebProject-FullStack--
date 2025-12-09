package org.spring.backendspring.admin.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.admin.repository.AdminEventRepository;
import org.spring.backendspring.admin.service.AdminEventService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.event.dto.EventDto;
import org.spring.backendspring.event.entity.EventEntity;
import org.spring.backendspring.event.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final AdminEventRepository adminEventRepository;

    @Override
    public EventDto createEvent(EventDto dto) {
        EventEntity entity = EventEntity.builder()
                .eventTitle(dto.getEventTitle())
                .eventLink(dto.getEventLink())
                .eventDescription(dto.getEventDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        return EventDto.fromEntity(adminEventRepository.save(entity));
    }

    @Override
    public PagedResponse<EventDto> getEventList(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<EventDto> eventPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            eventPage = adminEventRepository.findAll(pageable)
                    .map(EventDto::fromEntity);
        } else {
            eventPage = adminEventRepository
                    .findByEventTitleContainingIgnoreCase(keyword, pageable)
                    .map(EventDto::fromEntity);
        }

        return PagedResponse.of(eventPage);
    }

    @Override
    public EventDto getEventDetail(Long id) {
        return adminEventRepository.findById(id)
                .map(EventDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("이벤트가 존재하지 않습니다."));
    }

    @Override
    public EventDto updateEvent(Long id, EventDto dto) {
        EventEntity entity = adminEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이벤트가 존재하지 않습니다."));

        EventEntity updated = EventEntity.builder()
                .id(entity.getId())
                .eventTitle(dto.getEventTitle())
                .eventLink(dto.getEventLink())
                .eventDescription(dto.getEventDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createTime(entity.getCreateTime())
                .updateTime(LocalDateTime.now())
                .build();

        EventEntity saved = adminEventRepository.save(updated);

        return EventDto.fromEntity(saved);
    }

    @Override
    public void deleteEvent(Long id, String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("❌ 삭제 권한이 없습니다.");
        }
        adminEventRepository.deleteById(id);
    }
}
