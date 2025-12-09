package org.spring.backendspring.event.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.event.dto.EventDto;
import org.spring.backendspring.event.service.EventService;
import org.spring.backendspring.admin.service.AdminEventService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public PagedResponse<EventDto> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.getEventList(keyword, page, size);
    }

    @GetMapping("/{id}")
    public EventDto detail(@PathVariable Long id) {
        return eventService.getEventDetail(id);
    }
}
