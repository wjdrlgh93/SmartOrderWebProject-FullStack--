package org.spring.backendspring.crew.crewJoin.dto;

import lombok.*;
import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewJoinRequestDto {
    private Long id;
    private Long crewRequestId;
    private Long memberRequestId;
    private CrewEntity crewEntity;
    private MemberEntity memberEntity;
    private String message;
    private RequestStatus status;

    //더 넣고 싶은 정보는 상의 후 결정
    public static  CrewJoinRequestDto crewJoinRequestDto (CrewJoinRequestEntity crewJoinRequestEntity){
        return CrewJoinRequestDto.builder()
                .id(crewJoinRequestEntity.getId())
                .crewRequestId(crewJoinRequestEntity.getCrewEntity().getId())
                .memberRequestId(crewJoinRequestEntity.getMemberEntity().getId())
//                .crewEntity(crewJoinRequestEntity.getCrewEntity())
//                .memberEntity(crewJoinRequestEntity.getMemberEntity())
                .message(crewJoinRequestEntity.getMessage())
                .status(crewJoinRequestEntity.getStatus())
                .build();
    }
}
