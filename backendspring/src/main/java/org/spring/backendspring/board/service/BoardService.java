package org.spring.backendspring.board.service;

import java.io.IOException;

import org.spring.backendspring.board.dto.BoardDto;

import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    // C
    void insertBoard (BoardDto boardDto) throws IOException;
    // R
    Page<BoardDto> boardListPage(Pageable pageable, String subject, String search) ; 
    BoardDto boardDetail(Long boardId) throws IOException;

    // U
    void update(BoardDto boardDto) throws IOException;
    
    // D
    void deleteBoard(Long boardId);// 공지글 리스트
}
