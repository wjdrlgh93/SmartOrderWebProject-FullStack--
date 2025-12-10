package org.spring.backendspring.board.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.BoardImgDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.spring.backendspring.board.repository.BoardImgRepository;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.board.service.BoardService;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final MemberRepository memberRepository;
    
    private final AwsS3Service awsS3Service;

    @Value("${s3file.path.board}")
    private String path;


    @Override
    @Transactional
    public void insertBoard(BoardDto boardDto) throws IOException {

        MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
                .orElseThrow(IllegalArgumentException::new);
        boardDto.setMemberentity(memberEntity);

        if (boardDto.getBoardFile() == null || boardDto.getBoardFile().isEmpty()) {

            boardDto.setAttachFile(0);
            boardDto.setMemberentity(memberEntity);

            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            boardRepository.save(boardEntity);

        } else {
    
            MultipartFile boardFile = boardDto.getBoardFile();
            String originalFileName = boardFile.getOriginalFilename();

            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IllegalArgumentException("업로드된 파일의 원본 파일명이 유효하지 않습니다.");
            }


            String newFileName = awsS3Service.uploadFile(boardFile, path);
            boardDto.setAttachFile(1);

            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            boardEntity = boardRepository.save(boardEntity);
            BoardImgEntity boardImgEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                    .oldName(originalFileName)
                    .newName(newFileName)
                    .boardEntity(boardEntity)
                    .build());
            boardImgRepository.save(boardImgEntity);

        }
    }

    @Override
    public Page<BoardDto> boardListPage(Pageable pageable, String subject, String search) {
        Page<BoardEntity> boardEntities = null;

        if (subject == null || search == null || search.equals("")) {
            boardEntities = boardRepository.findAll(pageable);
        } else {
            if (subject.equals("title")) {
                boardEntities = boardRepository.findByTitleContaining(pageable, search);
            } else if (subject.equals("content")) {
                boardEntities = boardRepository.findByContentContaining(pageable, search);
            } else if  (subject.equals("nickName")){
                boardEntities = boardRepository.findByMemberEntity_NickNameContaining(pageable, search);
            } else {
                boardEntities = boardRepository.findAll(pageable);
            }
        }
        return boardEntities.map(boardEntity ->{
            BoardDto dto = BoardDto.toBoardDto(boardEntity);

            if (dto.getAttachFile() == 1 && dto.getNewFileName() != null) {
                String fileUrl;
                try {
                    fileUrl = awsS3Service.getFileUrl(dto.getNewFileName());
                    dto.setFileUrl(fileUrl);
                } catch (IOException ex) {
                     System.out.println("파일 처리 중 오류 발생: " + ex.getMessage());
                }
            }
            return dto;
        });

    }

    @Override
    public BoardDto boardDetail(Long boardId) throws IOException {
        boardRepository.updateHit(boardId);
        BoardEntity boardEntity = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 아이디가 존재하지 않음" + boardId));

        BoardDto boardDto = BoardDto.toBoardDto(boardEntity);
        
        String saveFileName = boardDto.getNewFileName();
        if(boardDto.getAttachFile() == 1 && saveFileName != null){
            String fileUrl = awsS3Service.getFileUrl(saveFileName);
            boardDto.setFileUrl(fileUrl);
        }
        return boardDto;


    }

    @Transactional
    @Override
    public void update(BoardDto boardDto) throws IOException {
        BoardEntity boardEntity = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 게시물이 없습니다"));

        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());

        if (boardDto.getBoardFile() != null && !boardDto.getBoardFile().isEmpty()) {
          
            MultipartFile newFile = boardDto.getBoardFile();
            Optional<BoardImgEntity> existImgOptional = boardImgRepository.findByBoardEntity(boardEntity);

            if (existImgOptional.isPresent()) {
                BoardImgEntity existFileEntity = existImgOptional.get();
               
                awsS3Service.deleteFile(existFileEntity.getNewName());
                boardImgRepository.delete(existFileEntity);
            }
            String originalFileName = newFile.getOriginalFilename();
            String newFileName =awsS3Service.uploadFile(newFile, path);
 
            boardEntity.setAttachFile(1);
            BoardImgEntity newFileEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                    .oldName(originalFileName)
                    .newName(newFileName)
                    .boardEntity(boardEntity)
                    .build());
            boardImgRepository.save(newFileEntity);
        } 
        boardRepository.save(boardEntity);

    }


    @Override
    public void deleteBoard(Long boardId) {

        BoardEntity boardEntity = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않음"));

        Optional<BoardImgEntity> fileOptional = boardImgRepository.findByBoardEntity(boardEntity);
        if (fileOptional.isPresent()) {
            BoardImgEntity boardImgEntity = fileOptional.get();
            awsS3Service.deleteFile(boardImgEntity.getNewName());
            boardImgRepository.delete(boardImgEntity);


        }
        boardRepository.delete(boardEntity);

    }
}
