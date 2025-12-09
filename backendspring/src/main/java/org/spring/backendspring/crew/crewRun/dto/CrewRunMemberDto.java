package org.spring.backendspring.crew.crewRun.dto;

import lombok.*;

import java.util.List;

import org.spring.backendspring.common.AttendeeStatus;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CrewRunMemberDto extends BasicTime {
    private Long id;


    private CrewRunEntity crewRunEntity; // 어떤 일정에 참석?
    private Long crewRunId;
    
    private MemberEntity memberEntity; // 누가 참석?
    private Long memberId;
    private String memberNickName;
    private List<String> memberImages;

    private AttendeeStatus status; // YES / NO 안쓸듯 걍 삭제 ㄱㄱ 일단 안함

    public static CrewRunMemberDto toCrewRunMemberDto(CrewRunMemberEntity crewRunMemberEntity){
        List<String> memberImages = crewRunMemberEntity.getMemberEntity().getProfileImagesList()
                    .stream().map(MemberProfileImageEntity::getNewName)
                    .toList();
        return CrewRunMemberDto.builder()
                .id(crewRunMemberEntity.getId())
                .crewRunId(crewRunMemberEntity.getCrewRunEntity().getId())
                .memberId(crewRunMemberEntity.getMemberEntity().getId())
                .memberNickName(crewRunMemberEntity.getMemberEntity().getNickName())
                .memberImages(memberImages)
                .build();
    }
}