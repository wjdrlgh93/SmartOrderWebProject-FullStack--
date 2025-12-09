package org.spring.backendspring.admin.service;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.common.dto.PagedResponse;

public interface AdminBoardService {

    void deleteBoardByAdmin(Long id);

    PagedResponse<BoardDto> findAllBoards(String keyword, String keyword2, int page, int size);

    NoticeBoardDto noticeWriteBoard(NoticeBoardDto noticeBoardDto);

    PagedResponse<NoticeBoardDto> noticeBoardList(String keyword, int page, int size);

    NoticeBoardDto findNoticeDetail(Long noticeId);

    NoticeBoardDto updateNoticeDetail(NoticeBoardDto noticeBoardDto);

    void deleteNotice(Long noticeId);
}
