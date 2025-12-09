package org.spring.backendspring.crew.crewBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.member.entity.MemberEntity;

import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "crew_board_comment_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewBoardCommentEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_board_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_board_id", nullable = false)
    private CrewBoardEntity crewBoardEntity; // 어떤 글의 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity; // 작성자

    @Column(nullable = false)
    private String content; // 댓글 내용

    public static CrewBoardCommentEntity toEntityC(CrewBoardCommentDto dto,
                                                  CrewBoardEntity crewBoardEntity) {

        return CrewBoardCommentEntity.builder()
                .crewBoardEntity(crewBoardEntity)
                .memberEntity(dto.getMemberEntity())
                .content(dto.getContent())
                .build();
    }

    public static CrewBoardCommentEntity toEntity(CrewBoardCommentDto dto) {

        return CrewBoardCommentEntity.builder()
                .crewBoardEntity(dto.getCrewBoardEntity())
                .memberEntity(dto.getMemberEntity())
                .content(dto.getContent())
                .build();
    }
}