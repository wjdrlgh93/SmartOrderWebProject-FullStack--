package org.spring.backendspring.crew.crewBoard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "crew_board_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewBoardEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_board_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String content; // 내용

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private CrewEntity crewEntity; // 소속 크루

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity; // 작성자

    @OneToMany(mappedBy = "crewBoardEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewBoardCommentEntity> crewBoardCommentEntities; // 댓글

    @OneToMany(mappedBy = "crewBoardEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CrewBoardImageEntity> crewBoardImageEntities; // 댓글

    public static CrewBoardEntity toCrewBoardEntity(CrewBoardDto dto) {
        return CrewBoardEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .crewEntity(CrewEntity.builder()
                        .id(dto.getCrewId())
                        .build())
                .memberEntity(MemberEntity.builder()
                        .id(dto.getMemberId())
                        .build())
                .crewBoardCommentEntities(dto.getCrewBoardCommentEntities())
                .crewBoardImageEntities(dto.getCrewBoardImageEntities())
                .build();
    }
    public static CrewBoardEntity toCrewBoardEntity2(CrewBoardDto dto) {
        return CrewBoardEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .crewEntity(CrewEntity.builder()
                        .id(dto.getCrewId())
                        .build())
                .memberEntity(MemberEntity.builder()
                        .id(dto.getMemberId())
                        .build())
                // .crewBoardCommentEntities(dto.getCrewBoardCommentEntities())
                .crewBoardImageEntities(dto.getCrewBoardImageEntities())
                .build();
    }
}