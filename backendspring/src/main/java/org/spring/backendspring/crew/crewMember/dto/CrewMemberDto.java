package org.spring.backendspring.crew.crewMember.dto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.crew.crew.entity.CrewImageEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.entity.MemberProfileImageEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewMemberDto extends BasicTime {

    private Long id;

    private Long crewId;
    private Long memberId;

//    private CrewEntity crewEntity;
    private String crewName;
    private String description;
    private String district;
    private List<CrewImageEntity> crewImageEntities;

    private MemberEntity memberEntity;

    private CrewRole roleInCrew; // LEADER/MEMBER

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // mycrew 목록 위한 이미지
    private List<String> crewImages;
    private List<String> fileUrl;
    private List<String> memberImages;
    private String memberNickName;

    private int crewMembers;

    public static CrewMemberDto toCrewMember(CrewMemberEntity crewMemberEntity) {
        List<String> crewImages = crewMemberEntity.getCrewEntity().getCrewImageEntities()
                    .stream().map(CrewImageEntity::getNewName)
                    .toList();
        
        List<String> memberImages = crewMemberEntity.getMemberEntity().getProfileImagesList()
                    .stream().map(MemberProfileImageEntity::getNewName)
                    .toList();
        int members = crewMemberEntity.getCrewEntity().getCrewMemberEntities().size();
        //보이고 싶은 정보 추가
        return CrewMemberDto.builder()
                .id(crewMemberEntity.getId())
                .crewId(crewMemberEntity.getCrewEntity().getId())
                .memberId(crewMemberEntity.getMemberEntity().getId())
                .crewName(crewMemberEntity.getCrewEntity().getName())
                .description(crewMemberEntity.getCrewEntity().getDescription())
                .district(crewMemberEntity.getCrewEntity().getDistrict())
                .roleInCrew(crewMemberEntity.getRoleInCrew())
                .createTime(crewMemberEntity.getCreateTime())
                .updateTime(crewMemberEntity.getUpdateTime())
                .crewImages(crewImages)
                .memberImages(memberImages)
                .memberNickName(crewMemberEntity.getMemberEntity().getNickName())
                .crewMembers(members)
                .build();
    }
    public static CrewMemberDto toCrewChatMember(CrewMemberEntity crewMemberEntity, AwsS3Service awsS3Service) {
        CrewMemberDto dto = toCrewMember(crewMemberEntity);
        List<String> memberImages = crewMemberEntity.getMemberEntity().getProfileImagesList()
            .stream().map(MemberProfileImageEntity::getNewName)
            .toList();
        List<String> urls = new ArrayList<>();
        if (dto.memberImages != null && !memberImages.isEmpty()) {
            urls = dto.memberImages.stream()
                    .map(name ->{
                        try {
                            return awsS3Service.getFileUrl(name);
                        } catch (IOException e) {
                            throw new IllegalArgumentException(e);
                        }
                    })
                    .toList();
        }   
        return CrewMemberDto.builder()
                .memberId(crewMemberEntity.getMemberEntity().getId())
                .fileUrl(urls)
                .memberImages(memberImages)
                .memberNickName(crewMemberEntity.getMemberEntity().getNickName())
                .build();
    }
}