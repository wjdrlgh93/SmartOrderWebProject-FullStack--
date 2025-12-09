package org.spring.backendspring.board.service;

import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.common.dto.PagedResponse;

public interface NoticeService {
    PagedResponse<NoticeBoardDto> findAllNoticeList(String keyword, int page, int size, String category);

    NoticeBoardDto findNoticeDetail(String category, Long noticeId);
}
