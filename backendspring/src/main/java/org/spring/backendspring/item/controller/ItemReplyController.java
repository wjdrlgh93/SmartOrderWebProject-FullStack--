package org.spring.backendspring.item.controller;

import org.spring.backendspring.board.dto.BoardReplyDto;
import org.spring.backendspring.item.dto.ItemReplyDto;
import org.spring.backendspring.item.service.ItemReplyService;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@Log4j2
@RestController
@RequestMapping("/api/itemReply")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ItemReplyController {

    private final ItemService itemService;
    private final ItemReplyService itemReplyService;

    // All Data return OneJSON type 
    @PostMapping("/addReply")
    public ResponseEntity<?> writeReply(@RequestBody ItemReplyDto itemReplyDto) {

        System.out.println(">>>" + itemReplyDto);
        itemReplyService.insertReply(itemReplyDto);
        return ResponseEntity.ok("reply Added");
    }

    @GetMapping("list/{itemId}")
    public ResponseEntity<Page<ItemReplyDto>> getReplyList(
            @PathVariable("itemId") Long itemId,
            @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemReplyDto> replyList = itemReplyService.getReplyPage(itemId, pageable);
        return ResponseEntity.ok(replyList);

    }

    @PutMapping("/updateReply")
    public ResponseEntity<String> updateReply(@RequestBody ItemReplyDto itemReplyDto) {

        try {
            // Service layer에서 권한 확인 및 수정 로직 수행
            itemReplyService.update(itemReplyDto);
            return ResponseEntity.ok("덧글 수정 성공");

        } catch (IllegalAccessError e) {
            log.warn("댓글 수정 권한 없음: replyId={}, memberId={}. 오류: {}",
                    itemReplyDto.getId(), itemReplyDto.getMemberId(), e.getMessage());
            // 403 Forbidden: 수정 권한이 없는 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            log.warn("댓글 수정 실패 (댓글 ID를 찾을 수 없음): replyId={}, 오류: {}",
                    itemReplyDto.getId(), e.getMessage());
            // 404 Not Found: 수정할 댓글을 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("댓글 수정 중 서버 오류 발생: replyId={}, 오류: {}",
                    itemReplyDto.getId(), e.getMessage());
            // 500 Internal Server Error
            return ResponseEntity.internalServerError().body("댓글 수정 중 서버 오류 발생");
        }
    }

     @DeleteMapping("/deleteReply/{replyId}")
     public ResponseEntity<String> deleteReply(
                @PathVariable("replyId") Long replyId,
             @RequestParam("memberId") Long memberId) {

            try{
            itemReplyService.deleteReply(replyId, memberId);
            return ResponseEntity.ok("덧글 삭제 성공");
            
        } catch (IllegalAccessError e){
            log.warn("댓글 삭제 권한 없음: replyId={}, memberId={}. 오류: {}", replyId, memberId, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403 Forbidden
        } catch ( IllegalArgumentException e){
            log.warn("댓글 삭제 실패 (댓글 ID를 찾을 수 없음): replyId={}, 오류: {}", replyId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found
        } catch (Exception e) {
            log.error("댓글 삭제 중 서버 오류 발생: replyId={}, 오류: {}", replyId, e.getMessage());
            return ResponseEntity.internalServerError().body("댓글 삭제 중 서버 오류 발생"); // 500 Internal Server Error
        }
    }

}

