package org.spring.backendspring.crew.crewBoard.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardCommentEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;

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
public class CrewBoardCommentDto {
    private Long id;

    private CrewBoardEntity crewBoardEntity; // 어떤 글의 댓글

    private MemberEntity memberEntity; // 작성자

    private String content; // 댓글 내용

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Long crewId;
    private Long boardId;
    private Long memberId;
    private String memberNickName;

    public static CrewBoardCommentDto toDto(CrewBoardCommentEntity entity) {

        return CrewBoardCommentDto.builder()
                .id(entity.getId())
                .crewId(entity.getCrewBoardEntity().getCrewEntity().getId())
                .memberId(entity.getMemberEntity().getId())
                .boardId(entity.getCrewBoardEntity().getId())
                .memberNickName(entity.getMemberEntity().getNickName())
                .content(entity.getContent())
                .createTime(entity.getCreateTime())
                .build();
    }
}
