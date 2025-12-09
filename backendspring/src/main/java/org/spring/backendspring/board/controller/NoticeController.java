package org.spring.backendspring.board.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.board.dto.NoticeBoardDto;
import org.spring.backendspring.board.service.NoticeService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    public String category = "NOTICE";
    private final NoticeService noticeService;

    @GetMapping("/noticeList")
    public ResponseEntity<?> getNoticeList(@RequestParam(name = "keyword", required = false) String keyword,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        PagedResponse<NoticeBoardDto> response = noticeService.findAllNoticeList(keyword, page, size, category);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("등록된 공지가 없습니다.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{noticeId}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable("noticeId") Long noticeId) {
        NoticeBoardDto noticeBoardDto = noticeService.findNoticeDetail(category, noticeId);
        if (noticeBoardDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 공지입니다.");
        }
        return ResponseEntity.ok(noticeBoardDto);
    }
}
