package org.spring.backendspring.board.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class BoardDto {



    

    private Long id;
 
    private String title;
  
    private String content;

    private String category;


    private Long memberId; 


    private int attachFile;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int replyCount;
    private int hit;
    private String memberNickName;

    private MultipartFile boardFile;
    private List<MultipartFile> boardFileList;
    private List<BoardImgDto> boardImgDtos;

    private String newFileName;
    private String oldFileName;


    private String fileUrl;


    private MemberEntity memberentity;


    @JsonIgnore
    private List<BoardReplyEntity> boardReplyEntities;
    @JsonIgnore
    private List<BoardImgEntity> boardImgEntities;



    public static BoardDto toBoardDto( BoardEntity boardEntity) {
        String newFileName = null;
        String oldFileName = null;

        List<BoardImgDto> boardImgDtos = null;

        if(boardEntity.getAttachFile() != 0 && 
            boardEntity.getBoardImgEntities() != null &&
            !boardEntity.getBoardImgEntities().isEmpty()){
                newFileName = boardEntity.getBoardImgEntities().get(0).getNewName();
                oldFileName = boardEntity.getBoardImgEntities().get(0).getOldName();

                boardImgDtos = boardEntity.getBoardImgEntities().stream()
                            .map(BoardImgDto::toBoardImgDto)
                            .collect(Collectors.toList());
            }

            return BoardDto.builder()
                    .id(boardEntity.getId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .category(boardEntity.getCategory())
                    .hit(boardEntity.getHit())
                    .boardImgDtos(boardImgDtos)
                    .memberId(boardEntity.getMemberEntity().getId())
                    .memberNickName(boardEntity.getMemberEntity().getNickName())
                    .memberId(boardEntity.getMemberEntity().getId())
                    .createTime(boardEntity.getCreateTime())
                    .updateTime(boardEntity.getUpdateTime())
                    .attachFile(boardEntity.getAttachFile()) // 0 또는 1
                    .fileUrl(boardEntity.getAttatchFileLink())
            
                    .newFileName(newFileName) 
                    .oldFileName(oldFileName)
            
                    .build();
    }
         
    
}
