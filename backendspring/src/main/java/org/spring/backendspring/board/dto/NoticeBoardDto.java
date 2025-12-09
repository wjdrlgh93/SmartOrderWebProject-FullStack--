package org.spring.backendspring.board.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NoticeBoardDto {
    private Long id;

    private String title;

    private String content;

    private String category;

    private Long memberId;

    private int hit;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static NoticeBoardDto toNoticeBoardDto(BoardEntity boardEntity) {
        return NoticeBoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .category(boardEntity.getCategory())
                .memberId(boardEntity.getMemberEntity().getId())
                .hit(boardEntity.getHit())
                .createTime(boardEntity.getCreateTime())
                .updateTime(boardEntity.getUpdateTime())
                .build();
    }
}
