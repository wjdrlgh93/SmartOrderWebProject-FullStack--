package org.spring.backendspring.admin.service.impl;

import org.spring.backendspring.admin.repository.AdminBoardRepository;
import org.spring.backendspring.admin.service.AdminBoardService;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService {

    private final BoardRepository boardRepository;
    private final AdminBoardRepository adminBoardRepository;

    String category = "NOTICE";

    @Override
    public PagedResponse<BoardDto> findAllBoards(String keyword, String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<BoardDto> boardPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            boardPage = boardRepository.findAll(pageable)
                    .map(BoardDto::toBoardDto);
        }

        else if ("all".equals(search)) {
            boardPage = adminBoardRepository
                    .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                    .map(BoardDto::toBoardDto);
        }

        else if ("title".equals(search)) {
            boardPage = adminBoardRepository
                    .findByTitleContainingIgnoreCase(keyword, pageable)
                    .map(BoardDto::toBoardDto);
        }

        else if ("content".equals(search)) {
            boardPage = adminBoardRepository
                    .findByContentContainingIgnoreCase(keyword, pageable)
                    .map(BoardDto::toBoardDto);
        }

        else {
            boardPage = boardRepository.findAll(pageable)
                    .map(BoardDto::toBoardDto);
        }

        return PagedResponse.of(boardPage);
    }

    @Override
    public void deleteBoardByAdmin(Long id) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }

    @Override
    public NoticeBoardDto noticeWriteBoard(NoticeBoardDto noticeBoardDto) {
        BoardEntity save = boardRepository.save(BoardEntity.toNoticeEntity(noticeBoardDto));
        return NoticeBoardDto.toNoticeBoardDto(save);
    }

    @Override
    public PagedResponse<NoticeBoardDto> noticeBoardList(String keyword, int page, int size) {
        PageRequest request = PageRequest.of(page, size, Sort.by("id").descending());
        Page<BoardEntity> boardEntities = null;
        if (keyword != null) {
            boardEntities = boardRepository.findByCategoryAndTitleContaining(request, category, keyword);
        } else {
            boardEntities = boardRepository.findByCategory(request, category);
        }
        if (boardEntities.isEmpty()) {
            throw new IllegalArgumentException("조회할 공지사항이 없습니다.");
        }
        return PagedResponse.of(boardEntities.map(NoticeBoardDto::toNoticeBoardDto));
    }

    @Override
    public NoticeBoardDto findNoticeDetail(Long noticeId) {
        BoardEntity boardEntity = boardRepository.findByCategoryAndId(category, noticeId)
                .orElseThrow(() -> new IllegalArgumentException("없는 공지사항입니다."));
        return NoticeBoardDto.toNoticeBoardDto(boardEntity);
    }

    @Override
    public NoticeBoardDto updateNoticeDetail(NoticeBoardDto noticeBoardDto) {
        BoardEntity boardEntity = boardRepository.findByCategoryAndId(category, noticeBoardDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("없는 공지사항입니다."));
        BoardEntity save = boardRepository.save(BoardEntity.toUpdateNoticeEntity(noticeBoardDto, boardEntity));
        return NoticeBoardDto.toNoticeBoardDto(save);
    }

    @Override
    public void deleteNotice(Long noticeId) {
        BoardEntity boardEntity = boardRepository.findByCategoryAndId(category, noticeId)
                .orElseThrow(() -> new IllegalArgumentException("없는 공지사항입니다."));
        boardRepository.delete(boardEntity);
    }
}
