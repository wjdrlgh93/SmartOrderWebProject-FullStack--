package org.spring.backendspring.member.dto;

import org.spring.backendspring.member.entity.MemberProfileImageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class MemberProfileImageDto {
    
    private Long id;

    private String newName; // 저장 파일명

    private String oldName; // 원본 파일명

    private Long memberId;

    public static MemberProfileImageDto toDto(MemberProfileImageEntity entity) {
        return MemberProfileImageDto.builder()
                .id(entity.getId())
                .newName(entity.getNewName())
                .oldName(entity.getOldName())
                .memberId(entity.getMemberEntity()!=null ? entity.getMemberEntity().getId() : null)
                .build();
    }
}
