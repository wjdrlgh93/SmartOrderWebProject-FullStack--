package org.spring.backendspring.crew.crewBoard.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/mycrew/{crewId}/board/{boardId}/comment")
@RequiredArgsConstructor
public class CrewBoardCommentController {
    
    private final CrewBoardCommentService crewBoardCommentService;

    @PostMapping("/write")
    public ResponseEntity<?> createComment(@RequestBody CrewBoardCommentDto commentDto,
                                           @PathVariable("crewId") Long crewId,
                                           @PathVariable("boardId") Long boardId,
                                           @AuthenticationPrincipal MyUserDetails userDetails) {
        
        Long loginUserId = userDetails.getMemberId();
        CrewBoardCommentDto createComment = crewBoardCommentService.createComment(commentDto, boardId, loginUserId);

        return ResponseEntity.ok(createComment);
    }

    @GetMapping("/list")
    public ResponseEntity<?> commentList(@PathVariable("crewId") Long crewId,
                                         @PathVariable("boardId") Long boardId,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "20") int size) {
        
        PagedResponse<CrewBoardCommentDto> crewBoardCommentList = crewBoardCommentService.commentList(crewId, boardId, page, size);

        Map<String, Object> commentList = new HashMap<>();

        commentList.put("commentList", crewBoardCommentList);

        return ResponseEntity.ok(commentList);
    }
    
    @GetMapping("/detail/{commentId}")
    public ResponseEntity<?> commentDetail(@PathVariable("commentId") Long id,
                                           @PathVariable("crewId") Long crewId,
                                           @PathVariable("boardId") Long boardId) {
        
        CrewBoardCommentDto commentDto = crewBoardCommentService.commentDetail(boardId, id);

        Map<String, CrewBoardCommentDto> response = new HashMap<>();

        response.put("commentDetail", commentDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CrewBoardCommentDto crewBoardCommentDto,
                                           @PathVariable("crewId") Long crewId,
                                           @PathVariable("commentId") Long id,
                                           @PathVariable("boardId") Long boardId,
                                           @AuthenticationPrincipal MyUserDetails userDetails) {
        Long loginUserId = userDetails.getMemberId();
        
        CrewBoardCommentDto updateCommentDto = crewBoardCommentService.updateComment(id, crewBoardCommentDto, loginUserId);

        Map<String, CrewBoardCommentDto> response = new HashMap<>();

        response.put("updateComment", updateCommentDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long id,
                                           @PathVariable("crewId") Long crewId,
                                           @PathVariable("boardId") Long boardId,
                                           @AuthenticationPrincipal MyUserDetails userDetails) {
        Long loginUserId = userDetails.getMemberId();

        crewBoardCommentService.deleteComment(id, loginUserId);

        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
