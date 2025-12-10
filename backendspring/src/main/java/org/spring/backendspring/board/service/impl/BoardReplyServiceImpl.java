package org.spring.backendspring.board.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.board.repository.BoardReplyRepository;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.board.service.BoardReplyService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService{

    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertReply(BoardReplyDto boardReplyDto) {

        Optional<BoardEntity> optinalBoardEntity 
            = boardRepository.findById(boardReplyDto.getBoardId());
        if(optinalBoardEntity.isPresent()){
            BoardEntity boardEntity = optinalBoardEntity.get();
            boardReplyDto.setBoardEntity(boardEntity);

        if(boardReplyDto.getMemberId() == null) {
            throw new IllegalArgumentException("존재하지 않는 회원ID");
        }
        Optional<MemberEntity> optionalMemberEntity 
                = memberRepository.findById(boardReplyDto.getMemberId());
        if(!optionalMemberEntity.isPresent()){
            throw new IllegalArgumentException("존재하지 않는 회원 ID입니다");
        }
            
        boardReplyDto.setMemberEntity(optionalMemberEntity.get());


        BoardReplyEntity boardReplyEntity = 
                BoardReplyEntity.toReplyEntity(boardReplyDto);
        return boardReplyRepository.save(boardReplyEntity).getId();
    
    }
    return null;
    }

    @Override
    public Page<BoardReplyDto> getReplyPage(Long boardId, Pageable pageable) {
        
        Page<BoardReplyEntity> replyEntitiesPage = 
        boardReplyRepository.findAllByBoardEntity_Id(boardId, pageable);

        Page<BoardReplyDto> replyDtoPage = replyEntitiesPage
            .map(BoardReplyDto::tBoardReplyDto); 

            return replyDtoPage;
    }

    @Override
    public void update(BoardReplyDto boardReplyDto) throws IOException {

        Optional<BoardReplyEntity> optionalBoardreplyEntity 
                = boardReplyRepository.findById(boardReplyDto.getId());
        if(optionalBoardreplyEntity.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 댓글 ID입니다.");
        }
        BoardReplyEntity boardReplyEntity = optionalBoardreplyEntity.get();

        Optional<BoardEntity> optionalBoardEntity 
                    = boardRepository.findById(boardReplyDto.getBoardId());
        if(optionalBoardEntity.isEmpty()){
            throw new IllegalArgumentException("댓글의 게시글이 존재하지 않습니다.");
        }

        boardReplyDto.setBoardEntity(optionalBoardEntity.get());

        if(boardReplyDto.getMemberId() == null ){
            throw new IllegalArgumentException("회원 정보가 필요합니다.");
        }

        Optional<MemberEntity> optionalMemberEntity
                = memberRepository.findById(boardReplyDto.getMemberId());

        if(optionalBoardEntity.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 회원 ID입니다.");
        }

        boardReplyDto.setMemberEntity(optionalMemberEntity.get());

        boardReplyEntity.updateFromDto(boardReplyDto);
        boardReplyRepository.save(boardReplyEntity);
    }

    @Override
    public void deleteReply(Long replyId, Long memberId) {

    if (replyId == null) {
        throw new IllegalArgumentException("삭제할 댓글 ID가 필요합니다.");
    }
    Optional<BoardReplyEntity> optionalBoardReplyEntity 
        = boardReplyRepository.findById(replyId);

    if (optionalBoardReplyEntity.isEmpty()) {
        throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 댓글 ID입니다.");
    }
    BoardReplyEntity boardReplyEntity = optionalBoardReplyEntity.get();
    if (memberId == null) {
        throw new IllegalArgumentException("회원 정보가 필요합니다.");
    }

    if (!boardReplyEntity.getMemberEntity().getId().equals(memberId)) {
        throw new IllegalAccessError("댓글 삭제 권한이 없습니다. (작성자만 삭제 가능)");
    }
    boardReplyRepository.delete(boardReplyEntity);

    } 
}
