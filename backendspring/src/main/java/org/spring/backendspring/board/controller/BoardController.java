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

// RestController is Data Only
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping("")
    // public ResponseEntity<Page<BoardDto>> boardList(@PageableDefault(size = 10,
    //                                             direction = Sort.Direction.DESC, sort = "createTime" ) 
    //                                 Pageable pageable) {

    //     Page<BoardDto> boardList = boardService.boardListPage(pageable, null, null);

    //     return ResponseEntity.ok(boardList);
    // }
    public ResponseEntity<Page<BoardDto>> boardList(
        @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "createTime") 
        Pageable pageable,
        @RequestParam(value = "subject",required = false) String subject, // 검색 기준 필드 (title, content, nickName)
        @RequestParam(value = "search",required = false) String search){
            Page<BoardDto> boardList = boardService.boardListPage(pageable, subject, search);
            return ResponseEntity.ok(boardList);
        }

    // search function
    // @GetMapping("/search")
    // public ResponseEntity<Page<BoardDto>> boardSearchList(
    //         @PageableDefault(size = 10,  direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
    //         @RequestParam(required = false) String subject,
    //         @RequestParam(required = false) String search) {

    //     Page<BoardDto> boardList = boardService.boardListPage(pageable, subject, search);
    //     return ResponseEntity.ok(boardList);
    // }

    // POST can  after Login
    @GetMapping("/newPost")
    public String newPost(BoardDto boardDto,
                          @AuthenticationPrincipal MyUserDetails myUserDetails,
                          Model model) {

        model.addAttribute("myUserDetails", myUserDetails);
        return "board/write";
    }

    // this Fragments -> form action="/board/write" Data request.
    @PostMapping("/write")
    public ResponseEntity<?> writeBoard(
                                        @ModelAttribute BoardDto boardDto,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        
        Long memberId = myUserDetails.getMemberId();
        boardDto.setMemberId(memberId);
        boardService.insertBoard(boardDto);
        return ResponseEntity.ok("DONE");
        
        //  if (!myUserDetails.getMemberId().equals(memberId)) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원의 정보가 일치하지 않습니다.");
        // }
        // boardService.insertBoard(boardDto);
        // return ResponseEntity.ok("DONE");
    }

    // URL: http://localhost:8088/api/board/detail/{id}
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<BoardDto> getBoardDetail(@PathVariable("boardId") Long boardId) throws IOException {

        BoardDto boardDto = boardService.boardDetail(boardId);
        
        return ResponseEntity.ok(boardDto);
    }

    // UPDATE
    @GetMapping("/update/{boardId}")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable("boardId") Long boardId, // PathVariable로 boardId를 받습니다.
                              @AuthenticationPrincipal MyUserDetails myUserDetails
                              ) throws IOException {

    try {
        BoardDto boardDto = boardService.boardDetail(boardId);
        if(!boardDto.getMemberId().equals(myUserDetails.getMemberId()))
            { throw new AccessDeniedException("수정권한이 없습니다."); }
        
        return ResponseEntity.ok(boardDto);
       } catch (EntityNotFoundException e) {
            // 게시글을 찾을 수 없는 경우 404 Not Found 응답
            return ResponseEntity.notFound().build();
        } 
         // AccessDeniedException은 throws 처리되어 상위 핸들러에서 처리.

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


    //* DELETE 요청: /board/{boardId}
    //* @param boardId 삭제할 게시글 ID
    //* @return 성공 응답
    // DELETE
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
            // TODO: handle exception
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // etc 500 error
            return ResponseEntity.internalServerError().body("게시글 삭제중 서버 오류 발생");
        }
    }
}