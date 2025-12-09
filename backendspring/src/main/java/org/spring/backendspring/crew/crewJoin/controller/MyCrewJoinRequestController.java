package org.spring.backendspring.crew.crewJoin.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.crew.crewJoin.service.CrewJoinRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew/{crewId}/joinRequest")
public class MyCrewJoinRequestController {

    private final CrewJoinRequestService crewJoinRequestService;


    //해당 크루 가입신청한 멤버
    // @GetMapping({"", "/"})
    // public ResponseEntity<?> myCrewJoinRequestList(@PathVariable("crewId") Long crewId) {
    //     List<CrewJoinRequestDto> myCrewJoinRequestDtoList = crewJoinRequestService.myCrewJoinList(crewId);
    //     Map<String, Object> myCrewjoinRequestMap = new HashMap<>();
    //     myCrewjoinRequestMap.put("myCrewJoinList", myCrewJoinRequestDtoList);
    //     return ResponseEntity.status(HttpStatus.OK).body(myCrewjoinRequestMap);
    // }
    
    @GetMapping({"", "/"})
    public ResponseEntity<?> myCrewJoinRequestList(@PathVariable("crewId") Long crewId,
                                    @PageableDefault(page = 0, size = 8, sort = "id")Pageable pageable,
                                    @RequestParam(name = "subject", required = false) String subject,
                                    @RequestParam(name = "search", required = false) String search
) {
        Page<CrewJoinRequestDto> page = crewJoinRequestService.myCrewJoinList(crewId, pageable, subject, search);
        int blockNum = 3;
        int nowPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();

        int startPage = ((nowPage - 1) / blockNum) * blockNum + 1;
        int endPage = Math.min(startPage + blockNum - 1, totalPages);

        Map<String, Object> myCrewjoinRequestMap = new HashMap<>();
        myCrewjoinRequestMap.put("myCrewJoinList", page);
        myCrewjoinRequestMap.put("nowPage", nowPage);
        myCrewjoinRequestMap.put("totalPages", totalPages);
        myCrewjoinRequestMap.put("startPage", startPage);
        myCrewjoinRequestMap.put("endPage", endPage);
        myCrewjoinRequestMap.put("pageSize", page.getSize());
        myCrewjoinRequestMap.put("totalElements", page.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(myCrewjoinRequestMap);
    }

    //크루 가입 승인
    @PostMapping("/approved")
    public ResponseEntity<?> myCrewJoinRequestApproved(
            //            @PathVariable("crewId") Long crewId,
            @RequestBody CrewJoinRequestDto joinDto,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberEntity().getId();
        crewJoinRequestService.crewJoinRequestApproved(joinDto, memberId);
        return ResponseEntity.ok("승인 완료");
    }

    //크루 가입 승인거절
    @PostMapping("/rejected")
    public ResponseEntity<?> myCrewJoinRequestRejected(
//            @PathVariable("crewId") Long crewId,
            @RequestBody CrewJoinRequestDto joinDto,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long memberId = myUserDetails.getMemberEntity().getId();
        crewJoinRequestService.crewJoinRequestRejected(joinDto, memberId);
        return ResponseEntity.ok("거절 완료");
    }

}
