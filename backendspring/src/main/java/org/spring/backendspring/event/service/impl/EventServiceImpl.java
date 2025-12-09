package org.spring.backendspring.event.service.impl;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.event.dto.EventDto;
import org.spring.backendspring.event.entity.EventEntity;
import org.spring.backendspring.event.repository.EventRepository;
import org.spring.backendspring.event.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public PagedResponse<EventDto> getEventList(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<EventDto> eventPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            eventPage = eventRepository.findAll(pageable)
                    .map(EventDto::fromEntity);
        } else {
            eventPage = eventRepository
                    .findByEventTitleContainingIgnoreCase(keyword, pageable)
                    .map(EventDto::fromEntity);
        }

        return PagedResponse.of(eventPage);
    }

    @Override
    public EventDto getEventDetail(Long id) {
        return eventRepository.findById(id)
                .map(EventDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("이벤트가 존재하지 않습니다."));
    }
}
