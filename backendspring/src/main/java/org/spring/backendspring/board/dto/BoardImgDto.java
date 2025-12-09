package org.spring.backendspring.board.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BoardImgDto {

    private Long id;
    private Long boardId;


    private MultipartFile boardFile;
    private String newName;
    private String oldName;

    // createTime
    private LocalDateTime createTime;
    // updateTime
    private LocalDateTime upDateTime;

    // N:1 
    @JsonIgnore // -> 순환참조문제 
    private BoardEntity boardEntity;


    public static BoardImgDto toBoardImgDto(BoardImgEntity boardImgEntity){
        return BoardImgDto.builder()
                        .id(boardImgEntity.getId())
                        .newName(boardImgEntity.getNewName())
                        .oldName(boardImgEntity.getOldName()) 
                        .createTime(boardImgEntity.getCreateTime())
                        .upDateTime(boardImgEntity.getUpdateTime())
                        .build();
    }

}
