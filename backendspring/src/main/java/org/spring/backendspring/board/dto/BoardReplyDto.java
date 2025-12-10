package org.spring.backendspring.board.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.member.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardReplyDto {

    private Long id;
    private Long boardId;
    private Long memberId;

    private String content;


    private LocalDateTime createTime;
    private LocalDateTime upDateTime;


    private BoardEntity boardEntity;

    private MemberEntity memberEntity;


    public static BoardReplyDto tBoardReplyDto (BoardReplyEntity boardReplyEntity){

        return BoardReplyDto.builder()
                            .id(boardReplyEntity.getId())
                            .content(boardReplyEntity.getContent())
                            .boardId(boardReplyEntity.getBoardEntity().getId())
                            .memberId(boardReplyEntity.getMemberEntity().getId())
                            .createTime(boardReplyEntity.getCreateTime())
                            .upDateTime(boardReplyEntity.getUpdateTime())
                            .build();
    }

   

    
}
