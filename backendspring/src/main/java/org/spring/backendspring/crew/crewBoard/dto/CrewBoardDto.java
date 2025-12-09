package org.spring.backendspring.crew.crewBoard.dto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardCommentEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardEntity;
import org.spring.backendspring.crew.crewBoard.entity.CrewBoardImageEntity;
import org.springframework.web.multipart.MultipartFile;

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
public class CrewBoardDto {

    private Long id;

    private String title; // 제목

    private String content; // 내용

    private CrewEntity crewEntity; // 소속 크루

//    private MemberEntity memberEntity; // 작성자

    private List<CrewBoardCommentEntity> crewBoardCommentEntities; // 댓글

    private List<CrewBoardImageEntity> crewBoardImageEntities ;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<MultipartFile> crewBoardFile;
    private List<String> originalFileName;
    private List<String> newFileName;

    private Long crewId;
    private String crewName;
    private Long memberId;
    private String memberNickName;
    private CrewRole role;
    private List<String> fileUrl;

    private int comments;

    public static CrewBoardDto toDto(CrewBoardEntity entity) {
        List<String> newFileName = entity.getCrewBoardImageEntities().stream()
                    .map(CrewBoardImageEntity::getNewName)
                    .toList();
        List<String> originalFileName = entity.getCrewBoardImageEntities().stream()
                    .map(CrewBoardImageEntity::getOldName)
                    .toList();

        int comments;
        if (entity.getCrewBoardCommentEntities() != null || entity.getCrewBoardCommentEntities().isEmpty()) {
            comments = entity.getCrewBoardCommentEntities().size();
        } else {
            comments = 0;
        }

        return CrewBoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .crewId(entity.getCrewEntity().getId())
                .crewName(entity.getCrewEntity().getName())
                .memberId(entity.getMemberEntity().getId())
//                .memberEntity(memberEntity)
                .memberNickName(entity.getMemberEntity().getNickName())
                // .crewBoardCommentEntities(entity.getCrewBoardCommentEntities())
                .originalFileName(originalFileName)
                .newFileName(newFileName)
                .comments(comments)
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
    public static CrewBoardDto toDto2(CrewBoardEntity entity) {

        List<String> newFileName = entity.getCrewBoardImageEntities().stream()
                .map(CrewBoardImageEntity::getNewName)
                .toList();
        List<String> originalFileName = entity.getCrewBoardImageEntities().stream()
                .map(CrewBoardImageEntity::getOldName)
                .toList();
        return CrewBoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .crewId(entity.getCrewEntity().getId())
                .crewName(entity.getCrewEntity().getName())
                .memberId(entity.getMemberEntity().getId())
                .memberNickName(entity.getMemberEntity().getNickName())
                // .crewBoardCommentEntities(entity.getCrewBoardCommentEntities())
                // .crewBoardImageEntities(entity.getCrewBoardImageEntities())
                .originalFileName(originalFileName)
                .newFileName(newFileName)
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
    public static CrewBoardDto toDtoS3(CrewBoardEntity entity, AwsS3Service awsS3Service) {
        CrewBoardDto dto = toDto2(entity);
        List<String> newFileName = entity.getCrewBoardImageEntities().stream()
                .map(CrewBoardImageEntity::getNewName)
                .toList();
        List<String> originalFileName = entity.getCrewBoardImageEntities().stream()
                .map(CrewBoardImageEntity::getOldName)
                .toList();
        List<String> urls = new ArrayList<>();
        if (dto.getNewFileName() != null && !newFileName.isEmpty()) {
            urls = dto.getNewFileName().stream()
                    .map(name -> {
                        try {
                            return awsS3Service.getFileUrl(name);
                        } catch (IOException e) {
                            throw new IllegalArgumentException(e);
                        }
                    })
                    .toList();
        }
        return CrewBoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .crewId(entity.getCrewEntity().getId())
                .crewName(entity.getCrewEntity().getName())
                .memberId(entity.getMemberEntity().getId())
                .memberNickName(entity.getMemberEntity().getNickName())
                .originalFileName(originalFileName)
                .newFileName(newFileName)
                .fileUrl(urls)
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
}
