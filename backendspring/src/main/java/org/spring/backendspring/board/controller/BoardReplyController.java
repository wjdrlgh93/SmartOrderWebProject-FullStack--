package org.spring.backendspring.board.controller;

import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.board.service.BoardReplyService;
import org.spring.backendspring.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardReplyController {

    private final BoardService boardService;
    private final BoardReplyService boardReplyService;


    // 모든 데이터를 하나의 JSON 객체로 받아옵니다.
    @PostMapping("/addReply")
    public ResponseEntity<?> writeReply( @RequestBody BoardReplyDto boardReplyDto){
    
    
        System.out.println(">>>" +boardReplyDto);
        boardReplyService.insertReply(boardReplyDto);
        return ResponseEntity.ok("reply Added.");
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<Page<BoardReplyDto>> getReplyList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(
                size = 10, 
                sort = "createTime", 
                direction = Sort.Direction.DESC )
            Pageable pageable){

        Page<BoardReplyDto> replyList = boardReplyService.getReplyPage(boardId, pageable);
        return ResponseEntity.ok(replyList);
    }

    // /api/reply/updateReply
    @PutMapping("/updateReply")
    public ResponseEntity<String> updateReply(@RequestBody BoardReplyDto boardReplyDto) {

        try {
            // Service layer에서 권한 확인 및 수정 로직 수행
            boardReplyService.update(boardReplyDto);
            return ResponseEntity.ok("덧글 수정 성공");

        } catch (IllegalAccessError e) {
            log.warn("댓글 수정 권한 없음: replyId={}, memberId={}. 오류: {}", 
                     boardReplyDto.getId(), boardReplyDto.getMemberId(), e.getMessage());
            // 403 Forbidden: 수정 권한이 없는 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); 

        } catch (IllegalArgumentException e) {
            log.warn("댓글 수정 실패 (댓글 ID를 찾을 수 없음): replyId={}, 오류: {}", 
                     boardReplyDto.getId(), e.getMessage());
            // 404 Not Found: 수정할 댓글을 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 

        } catch (Exception e) {
            log.error("댓글 수정 중 서버 오류 발생: replyId={}, 오류: {}", 
                      boardReplyDto.getId(), e.getMessage());
            // 500 Internal Server Error
            return ResponseEntity.internalServerError().body("댓글 수정 중 서버 오류 발생"); 
        }
    }



    //  api/reply/deleteReply/13.
    @DeleteMapping("/deleteReply/{replyId}")
    public ResponseEntity<String> deleteReply(
        @PathVariable("replyId") Long replyId,
        @RequestParam("memberId") Long memberId ){
    
    try{
        boardReplyService.deleteReply(replyId, memberId);
        return ResponseEntity.ok("덧글 삭제 성공");
        
    } catch (IllegalAccessError e){
        log.warn("댓글 삭제 권한 없음: replyId={}, memberId={}. 오류: {}", replyId, memberId, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403 Forbidden
    } catch ( IllegalArgumentException e){
        log.warn("댓글 삭제 실패 (댓글 ID를 찾을 수 없음): replyId={}, 오류: {}", replyId, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found
    } catch( Exception e) {
        log.error("댓글 삭제 중 서버 오류 발생: replyId={}, 오류: {}", replyId, e.getMessage());
            return ResponseEntity.internalServerError().body("댓글 삭제 중 서버 오류 발생"); // 500 Internal Server Error
    }

    }
    
    
        
    
}
