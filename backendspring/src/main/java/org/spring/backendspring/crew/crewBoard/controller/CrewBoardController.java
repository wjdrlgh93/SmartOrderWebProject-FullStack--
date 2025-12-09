package org.spring.backendspring.crew.crewBoard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardDto;
import org.spring.backendspring.crew.crewBoard.service.CrewBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/mycrew/{crewId}/board")
@RequiredArgsConstructor
public class CrewBoardController {

    private final CrewBoardService crewBoardService;

    @GetMapping({"", "/", "/list"})
    public ResponseEntity<?> boardListByCrew
                    (@PathVariable("crewId") Long crewId,
                     @RequestParam(name = "subject", required = false) String subject,
                     @RequestParam(name = "keyword", required = false) String keyword,
                     @RequestParam(name = "page", defaultValue = "0") int page,
                     @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<CrewBoardDto> crewBoardDtoList = crewBoardService.boardListByCrew(crewId, subject, keyword, page, size);

        Map<String, Object> crewBoardList = new HashMap<>();

        crewBoardList.put("crewBoardList", crewBoardDtoList);

        return ResponseEntity.ok(crewBoardList);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(
                @PathVariable("crewId") Long crewId,
                @RequestPart("title") String title,
                @RequestPart("content") String content,
                @RequestParam(value = "crewBoardFile", required = false) List<MultipartFile> newImages,
                @AuthenticationPrincipal MyUserDetails userDetails
    ) throws IOException {
        CrewBoardDto crewBoardDto = new CrewBoardDto();
        crewBoardDto.setTitle(title);
        crewBoardDto.setContent(content);
        crewBoardDto.setCrewBoardFile(newImages);

        Long loginUserId = userDetails.getMemberId();
        CrewBoardDto createBoard = crewBoardService.createBoard(crewId, crewBoardDto, loginUserId, newImages);

        return ResponseEntity.ok(createBoard);
    }

    @GetMapping("/detail/{boardId}")
    public ResponseEntity<?> boardDetail(@PathVariable("boardId") Long id,
                                         @PathVariable("crewId") Long crewId) {

        CrewBoardDto crewBoardDto = crewBoardService.boardDetail(crewId, id);

        Map<String, CrewBoardDto> response = new HashMap<>();

        response.put("boardDetail", crewBoardDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{crewBoardId}")
    public ResponseEntity<?> updateBoard(
                @PathVariable("crewBoardId") Long id,
                @PathVariable("crewId") Long crewId,
                @RequestPart("title") String title,
                @RequestPart("content") String content,
                @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
                @RequestParam(value = "deleteImageName", required = false) List<String> deleteImageName,
                @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {
        
        CrewBoardDto crewBoardDto = new CrewBoardDto();
        crewBoardDto.setTitle(title);
        crewBoardDto.setContent(content);

        Long loginUserId = userDetails.getMemberId();

        
        CrewBoardDto updateBoardDto = crewBoardService.updateBoard(id, crewId, crewBoardDto, loginUserId, newImages,
        deleteImageName);
        
        if (!loginUserId.equals(updateBoardDto.getMemberId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정 권한이 없습니다.");
        }
        Map<String, CrewBoardDto> response = new HashMap<>();

        response.put("updatedBoard", updateBoardDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{crewBoardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("crewBoardId") Long id,
                                         @PathVariable("crewId") Long crewId,
                                         @AuthenticationPrincipal MyUserDetails userDetails) {

        Long loginUserId = userDetails.getMemberId();
        CrewBoardDto crewBoardDto = crewBoardService.boardDetail(crewId, id);

        if (!loginUserId.equals(crewBoardDto.getMemberId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 권한이 없습니다.");
        }
        crewBoardService.deleteBoard(id, crewId, loginUserId);

        return ResponseEntity.ok("게시글 삭제 완료");
    }

}
