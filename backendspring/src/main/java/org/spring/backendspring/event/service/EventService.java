package org.spring.backendspring.event.service;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.event.dto.EventDto;
import java.util.List;

public interface EventService {

    PagedResponse<EventDto> getEventList(String keyword, int page, int size);

    EventDto getEventDetail(Long id);
}

