package org.spring.backendspring.member.entity;

import org.spring.backendspring.member.dto.MemberProfileImageDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "member_profile_image_tb")
public class MemberProfileImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_image_id")
    private Long id;


    private String newName; // 저장 파일명

    private String oldName; // 원본 파일명

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private MemberEntity memberEntity;
    
    public static MemberProfileImageDto toDto(MemberProfileImageEntity entity) {
        return MemberProfileImageDto.builder()
                .id(entity.getId())
                .newName(entity.getNewName())
                .oldName(entity.getOldName())
                .memberId(entity.getMemberEntity()!=null ? entity.getMemberEntity().getId() : null)
                .build();
    }
    
    public static MemberProfileImageEntity toEntity(MemberProfileImageDto dto, MemberEntity memberEntity) {
        return MemberProfileImageEntity.builder()
                .id(dto.getId())
                .newName(dto.getNewName())
                .oldName(dto.getOldName())
                .memberEntity(memberEntity)
                .build();
    }
}
