package org.spring.backendspring.admin.controller;

import java.io.IOException;

import org.spring.backendspring.admin.repository.AdminBoardRepository;
import org.spring.backendspring.admin.service.AdminBoardService;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.board.service.BoardService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;
    private final BoardService boardService;
    private final AdminBoardRepository adminBoardRepository;

    @GetMapping("/boardList")
    public ResponseEntity<PagedResponse<BoardDto>> getAllBoards(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<BoardDto> boardList = adminBoardService.findAllBoards(keyword, search, page, size);
        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/detail/{boardId}")
    public ResponseEntity<BoardDto> getBoardDetail(@PathVariable("boardId") Long boardId) throws IOException {

        BoardDto boardDto = boardService.boardDetail(boardId);
        return ResponseEntity.ok(boardDto);
    }

    @DeleteMapping("/delete/{boardid}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardid") Long id) {
        adminBoardService.deleteBoardByAdmin(id);
        return ResponseEntity.ok("관리자에 의해 게시물이 삭제 되었습니다");
    }

    @PostMapping("/write/notice")
    public ResponseEntity<?> writeNotice(@RequestBody NoticeBoardDto noticeBoardDto) {
        NoticeBoardDto boardWrite = adminBoardService.noticeWriteBoard(noticeBoardDto);
        return ResponseEntity.ok("공지사항 등록이 완료되었습니다.");
    }

    @GetMapping("/notice/list")
    public ResponseEntity<?> noticeList(@RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PagedResponse<NoticeBoardDto> response = adminBoardService.noticeBoardList(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notice/detail/{noticeId}")
    public ResponseEntity<?> noticeDetail(@PathVariable("noticeId") Long noticeId) {
        NoticeBoardDto boardDto = adminBoardService.findNoticeDetail(noticeId);
        if (boardDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("공지사항 글 조회 실패");
        }
        return ResponseEntity.ok(boardDto);
    }

    @PutMapping("/notice/update")
    public ResponseEntity<?> noticeUpdate(@RequestBody NoticeBoardDto noticeBoardDto) {
        NoticeBoardDto boardDto = adminBoardService.updateNoticeDetail(noticeBoardDto);
        if (boardDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("공지사항 수정 실패");
        }
        return ResponseEntity.ok("공지사항 수정이 정상적으로 반영되었습니다.");
    }

    @DeleteMapping("/notice/delete/{noticeId}")
    public ResponseEntity<?> noticeDelete(@PathVariable("noticeId") Long noticeId) {
        adminBoardService.deleteNotice(noticeId);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    @GetMapping("/total")
    public long getTotalBoards() {
        return adminBoardRepository.countAll();
    }

    @GetMapping("/today")
    public long getTodayBoards() {
        return adminBoardRepository.countToday();
    }
}
