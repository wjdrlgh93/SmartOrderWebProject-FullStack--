package org.spring.backendspring.admin.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.event.dto.EventDto;
import java.util.List;

public interface AdminEventService {

    EventDto createEvent(EventDto dto);

    public PagedResponse<EventDto> getEventList(String keyword, int page, int size);

    EventDto getEventDetail(Long id);

    EventDto updateEvent(Long id, EventDto dto);

}
