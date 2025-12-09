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
    
    // private static final String FILE_PATH = "C:/full/upload/";
    // S3 bucket
    private final AwsS3Service awsS3Service;

    @Value("${s3file.path.board}")
    private String path;


    @Override
    @Transactional
    public void insertBoard(BoardDto boardDto) throws IOException {

        // Membmer Check 
        MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
                .orElseThrow(IllegalArgumentException::new);
        boardDto.setMemberentity(memberEntity);

        // if file is Empty..
        if (boardDto.getBoardFile() == null || boardDto.getBoardFile().isEmpty()) {

            boardDto.setAttachFile(0);
            boardDto.setMemberentity(memberEntity);

            // DTO -> Entity
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            boardRepository.save(boardEntity);

        } else {
            // there has some File..
            // Bring FILE DTO
    
            MultipartFile boardFile = boardDto.getBoardFile();
            String originalFileName = boardFile.getOriginalFilename();

            if (originalFileName == null || originalFileName.isEmpty()) {
                // 파일은 있으나 파일명이 없는 예외적인 경우 처리 (혹은 오류 던지기)
                throw new IllegalArgumentException("업로드된 파일의 원본 파일명이 유효하지 않습니다.");
            }

            // this methold handle S3 Service Class so remove later 
            // UUID uuid = UUID.randomUUID();
            // String newFileName = uuid + "_" + originalFileName;
            // String filePath = FILE_PATH + newFileName;

            // Acutally FileSave..
            // boardFile.transferTo(new File(filePath)); // saveFile to Path ...
            // upload S3 and bring Get New FileName(include UUID)
            String newFileName = awsS3Service.uploadFile(boardFile, path);
            boardDto.setAttachFile(1);

            // Board Save After -> FileSave
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDto);
            // SAVE
            boardEntity = boardRepository.save(boardEntity);
            // FileSave
            BoardImgEntity boardImgEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                    .oldName(originalFileName)
                    .newName(newFileName)
                    .boardEntity(boardEntity)
                    .build());
            boardImgRepository.save(boardImgEntity); // SAVE FILE 

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
        // Change -> Dto ( also Setting URL )
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
        // 기존게시물이 있니?
        BoardEntity boardEntity = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 게시물이 없습니다"));

        // DTO Value -> Entity Update 
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());

        // there is NewFile?
        // File fix Logic 
        if (boardDto.getBoardFile() != null && !boardDto.getBoardFile().isEmpty()) {
          
            // there has NewFile 
            MultipartFile newFile = boardDto.getBoardFile();
            // Original File Check First.... then Delete 
            Optional<BoardImgEntity> existImgOptional = boardImgRepository.findByBoardEntity(boardEntity);

            // there has OldFile..
            if (existImgOptional.isPresent()) {
                BoardImgEntity existFileEntity = existImgOptional.get();
                // Psycially Image Delete ->
               
                // Delete from S3 Oldfile..
                awsS3Service.deleteFile(existFileEntity.getNewName());
                // Also you Delete DB Data info(img)
                boardImgRepository.delete(existFileEntity);
            }
            // and Replace NewFile 
            String originalFileName = newFile.getOriginalFilename();
            String newFileName =awsS3Service.uploadFile(newFile, path);
 
            // And Change AttachFile Statue...
            boardEntity.setAttachFile(1);
            // Also SAVE New IMG Data into DB...-> 
            BoardImgEntity newFileEntity = BoardImgEntity.toInsertFile(BoardImgDto.builder()
                    .oldName(originalFileName)
                    .newName(newFileName)
                    .boardEntity(boardEntity)
                    .build());
            boardImgRepository.save(newFileEntity);
        } 
        // Save Entity 
        // Alredy Have id, JPA run UPDATE Query.
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
            // DB delete
            boardImgRepository.delete(boardImgEntity);


        }
        // Board Entity Delete 
        boardRepository.delete(boardEntity);

    }
}
