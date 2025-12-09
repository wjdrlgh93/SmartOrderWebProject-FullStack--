package org.spring.backendspring.board.service;

import java.io.IOException;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.BoardReplyDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReplyService {

    Long insertReply(BoardReplyDto boardReplyDto);

    Page<BoardReplyDto> getReplyPage(Long boardId, Pageable pageable);
    
    // U
    void update(BoardReplyDto boardReplyDto) throws IOException;
    
    // D
    void deleteReply(Long replyId, Long memberId);




}
