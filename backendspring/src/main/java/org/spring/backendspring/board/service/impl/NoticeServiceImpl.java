package org.spring.backendspring.board.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.board.service.NoticeService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final BoardRepository boardRepository;

    @Override
    public PagedResponse<NoticeBoardDto> findAllNoticeList(String keyword, int page, int size, String category) {
        PageRequest request = PageRequest.of(page, size, Sort.by("id").descending());
        Page<BoardEntity> noticePage;
        if (keyword != null) {
            noticePage = boardRepository.findByCategoryAndTitleContaining(request, category, keyword);
        } else {
            noticePage = boardRepository.findByCategory(request, category);
        }

        if (noticePage.isEmpty()) {
            throw new IllegalArgumentException("등록된 공지사항이 없습니다.");
        }
        return PagedResponse.of(noticePage.map(NoticeBoardDto::toNoticeBoardDto));
    }

    @Override
    public NoticeBoardDto findNoticeDetail(String category, Long noticeId) {
        BoardEntity boardEntity = boardRepository.findByCategoryAndId(category, noticeId)
                .orElseThrow(() -> new IllegalArgumentException("없는 공지입니다."));
        boardRepository.updateHit(boardEntity.getId());
        return NoticeBoardDto.toNoticeBoardDto(boardEntity);
    }

}
