package org.spring.backendspring.crew.crewRun.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewRun.dto.CrewRunDto;
import org.spring.backendspring.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "crew_run_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewRunEntity extends BasicTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crew_run_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 일정 제목


    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt; // 시작 일시


    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt; // 종료 일시

//    @Column(name = "all_day")
//    private Boolean allDay; // 종일 여부

    private String place; // 모임 장소 텍스트

    private String routeHint; // 짧은 코스 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private CrewEntity crewEntity; // 소속 크루

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity; // 일정 생성자

    public static CrewRunEntity createCrewRun(CrewRunDto crewRunDto){
        return CrewRunEntity.builder()
                .title(crewRunDto.getTitle())
                .startAt(crewRunDto.getStartAt())
                .endAt(crewRunDto.getEndAt())
                .place(crewRunDto.getPlace())
                .routeHint(crewRunDto.getRouteHint())
                .crewEntity(crewRunDto.getCrewEntity())
                .memberEntity(crewRunDto.getMemberEntity())
                .build();
    }

    public static CrewRunEntity updateCrewRun(CrewRunDto crewRunDto){
        return CrewRunEntity.builder()
                .id(crewRunDto.getId())
                .title(crewRunDto.getTitle())
                .startAt(crewRunDto.getStartAt())
                .endAt(crewRunDto.getEndAt())
                .place(crewRunDto.getPlace())
                .routeHint(crewRunDto.getRouteHint())
                .crewEntity(crewRunDto.getCrewEntity())
                .memberEntity(crewRunDto.getMemberEntity())
                .build();

    }
}
