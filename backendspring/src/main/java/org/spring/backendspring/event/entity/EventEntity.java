package org.spring.backendspring.event.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event_tb")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventTitle;

    private String eventLink;

    private String eventDescription;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
