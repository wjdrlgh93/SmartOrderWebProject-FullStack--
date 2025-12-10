package org.spring.backendspring.board.controller;


import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.repository.BoardRepository;
import org.spring.backendspring.board.service.BoardService;
import org.spring.backendspring.config.security.MyUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping("")

    public ResponseEntity<Page<BoardDto>> boardList(
        @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "createTime") 
        Pageable pageable,
        @RequestParam(value = "subject",required = false) String subject,
        @RequestParam(value = "search",required = false) String search){
            Page<BoardDto> boardList = boardService.boardListPage(pageable, subject, search);
            return ResponseEntity.ok(boardList);
        }

    @GetMapping("/newPost")
    public String newPost(BoardDto boardDto,
                          @AuthenticationPrincipal MyUserDetails myUserDetails,
                          Model model) {

        model.addAttribute("myUserDetails", myUserDetails);
        return "board/write";
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeBoard(
                                        @ModelAttribute BoardDto boardDto,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        
        Long memberId = myUserDetails.getMemberId();
        boardDto.setMemberId(memberId);
        boardService.insertBoard(boardDto);
        return ResponseEntity.ok("DONE");
        
    }

    @GetMapping("/detail/{boardId}")
    public ResponseEntity<BoardDto> getBoardDetail(@PathVariable("boardId") Long boardId) throws IOException {

        BoardDto boardDto = boardService.boardDetail(boardId);
        
        return ResponseEntity.ok(boardDto);
    }

    @GetMapping("/update/{boardId}")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable("boardId") Long boardId,
                              @AuthenticationPrincipal MyUserDetails myUserDetails
                              ) throws IOException {

    try {
        BoardDto boardDto = boardService.boardDetail(boardId);
        if(!boardDto.getMemberId().equals(myUserDetails.getMemberId()))
            { throw new AccessDeniedException("수정권한이 없습니다."); }
        
        return ResponseEntity.ok(boardDto);
       } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } 

    }
    @PutMapping("/updatePost")
    public ResponseEntity<?> updateBoard(@ModelAttribute BoardDto boardDto,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        
        Long memberId = myUserDetails.getMemberId();
         if (!memberId.equals(boardDto.getMemberId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 권한이 없습니다.");
         }
         boardService.update(boardDto);
        return ResponseEntity.ok("UPDATE DONE");

    }


    @DeleteMapping("/detail/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
                                                
        Long authMemberId = myUserDetails.getMemberId();
        Long memberId = boardService.boardDetail(boardId).getMemberId();

        if (!authMemberId.equals(memberId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 권한이 없습니다.");
        }

        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("게시글 삭제 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("게시글 삭제중 서버 오류 발생");
        }
    }
}