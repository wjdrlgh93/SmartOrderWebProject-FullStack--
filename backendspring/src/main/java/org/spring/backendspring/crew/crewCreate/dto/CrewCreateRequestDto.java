package org.spring.backendspring.crew.crewCreate.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.common.RequestStatus;

import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewCreateRequestDto {

    private Long id;
    private String crewName; // 크루명
    private String message; // 메모
    private String district;
    private Long memberId;
    private MemberEntity memberEntity; // 개설자
    private RequestStatus status; // PENDING/APPROVED/REJECTED
    
    private LocalDateTime createTime;
    private LocalDateTime upDateTime;

    public static CrewCreateRequestDto toDto(CrewCreateRequestEntity entity) {

        return CrewCreateRequestDto.builder()
                .id(entity.getId())
                .crewName(entity.getCrewName())
                .message(entity.getMessage())
                .district(entity.getDistrict())
                .memberId(entity.getMemberEntity().getId())
                .status(entity.getStatus())
                .createTime(entity.getCreateTime())
                .upDateTime(entity.getUpdateTime())
                .build();
    }
}
