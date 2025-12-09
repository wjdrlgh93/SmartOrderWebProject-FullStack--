package org.spring.backendspring.crew.crewRun.dto;

import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CrewRunDto extends BasicTime {
    private Long id;

    private String title; // 일정 제목

    private LocalDateTime startAt; // 시작 일시

    private LocalDateTime endAt; // 종료 일시

    private String place; // 모임 장소 텍스트

    private String routeHint; // 짧은 코스 설명

    private CrewEntity crewEntity; // 소속 크루

    private MemberEntity memberEntity; // 일정 생성자

    private Long crewId;

    private Long memberId;

    public static CrewRunDto toCrewRunDto(CrewRunEntity crewRunEntity){
        return CrewRunDto.builder()
                .id(crewRunEntity.getId())
                .title(crewRunEntity.getTitle())
                .startAt(crewRunEntity.getStartAt())
                .endAt(crewRunEntity.getEndAt())
                .place(crewRunEntity.getPlace())
                .routeHint(crewRunEntity.getRouteHint())
                .crewId(crewRunEntity.getCrewEntity().getId())
                .memberId(crewRunEntity.getMemberEntity().getId())
                .build();

    }
}
