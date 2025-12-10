package org.spring.backendspring.board.service;

import java.io.IOException;

import org.spring.backendspring.board.dto.BoardDto;

import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    void insertBoard (BoardDto boardDto) throws IOException;
    Page<BoardDto> boardListPage(Pageable pageable, String subject, String search) ; 
    BoardDto boardDetail(Long boardId) throws IOException;

    void update(BoardDto boardDto) throws IOException;
    
    void deleteBoard(Long boardId);
}
