package org.spring.backendspring.crew.crew.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.crew.crewRun.entity.CrewRunEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "crew_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 크루명

    private String description; // 크루 설명

    @Column(nullable = false)
    private String district; // 지역(구)

    private int isCrewImg; // 0/1 대표 이미지

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity; // 개설자

    // Crew 1:N CrewImage
    @OneToMany(mappedBy = "crewEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewImageEntity> crewImageEntities;

    // Crew 1:N CrewMember
    @OneToMany(mappedBy = "crewEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewMemberEntity> crewMemberEntities;

    // Crew 1:N CrewJoinRequest
    @JsonIgnore
    @OneToMany(mappedBy = "crewEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewJoinRequestEntity> crewJoinRequestEntities;

    // Crew 1:N CrewRun
    @OneToMany(mappedBy = "crewEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewRunEntity> crewRunEntities;

    // Crew 1:N CrewBoard
    @OneToMany(mappedBy = "crewEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewBoardEntity> crewBoardEntities;

    public static CrewEntity toCrewEntity(CrewDto dto) {
        return CrewEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .district(dto.getDistrict())
                .isCrewImg(0)
                .memberEntity(dto.getMemberEntity())
                .crewImageEntities(dto.getCrewImageEntities())
                .crewMemberEntities(dto.getCrewMemberEntities())
//                .crewJoinRequestEntities(dto.getCrewJoinRequestEntities())
                .crewRunEntities(dto.getCrewRunEntities())
                .crewBoardEntities(dto.getCrewBoardEntities())
                .build();
    }

    public static CrewEntity toCrewEntityImg(CrewDto dto) {
        return CrewEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .district(dto.getDistrict())
                .isCrewImg(1)
                .memberEntity(dto.getMemberEntity())
                .crewImageEntities(dto.getCrewImageEntities())
                .crewMemberEntities(dto.getCrewMemberEntities())
//                .crewJoinRequestEntities(dto.getCrewJoinRequestEntities())
                .crewRunEntities(dto.getCrewRunEntities())
                .crewBoardEntities(dto.getCrewBoardEntities())
                .build();
    }

}