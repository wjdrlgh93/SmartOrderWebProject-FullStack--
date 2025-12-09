package org.spring.backendspring.event.dto;

import lombok.*;
import org.spring.backendspring.event.entity.EventEntity;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String eventTitle;
    private String eventLink;
    private String eventDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static EventDto fromEntity(EventEntity e) {
        return EventDto.builder()
                .id(e.getId())
                .eventTitle(e.getEventTitle())
                .eventLink(e.getEventLink())
                .eventDescription(e.getEventDescription())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .build();
    }
}
