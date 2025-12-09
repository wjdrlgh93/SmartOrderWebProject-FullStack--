package org.spring.backendspring.crew.crewRun.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewRun.dto.CrewRunMemberDto;
import org.spring.backendspring.crew.crewRun.service.CrewRunMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mycrew/{crewId}/run/{runId}/member")
public class MyCrewRunMemberController {

    private final CrewRunMemberService crewRunMemberService;



    //크루 런닝 스케줄 참가자
    @GetMapping({"","/"})
    public ResponseEntity<?> myCrewRunMember(
            @PathVariable("crewId") Long crewId,
            @PathVariable("runId") Long runId,
         @PageableDefault(page = 0, size = 8, sort = "id")Pageable pageable){
        Page<CrewRunMemberDto> page =
                crewRunMemberService.findCrewRunMemberList(crewId,runId,pageable);
        int blockNum = 3;
        int nowPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();

        int startPage = ((nowPage - 1) / blockNum) * blockNum + 1;
        int endPage = Math.min(startPage + blockNum - 1, totalPages);
                
        Map<String,Object> crewRunMemberListMap = new HashMap<>();
        crewRunMemberListMap.put("crewRunMember", page);
        crewRunMemberListMap.put("nowPage", nowPage);
        crewRunMemberListMap.put("totalPages", totalPages);
        crewRunMemberListMap.put("startPage", startPage);
        crewRunMemberListMap.put("endPage", endPage);
        crewRunMemberListMap.put("pageSize", page.getSize());
        crewRunMemberListMap.put("totalElements", page.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(crewRunMemberListMap);
    }

    //크루 런닝 참가
    @PostMapping("/{memberId}/yes")
    public ResponseEntity<?> myCrewRunMemberYes(
//            @PathVariable("crewId") Long crewId,
                                            @PathVariable("runId") Long runId,
                                            @PathVariable("memberId") Long memberId
    ){
        crewRunMemberService.insertCrewMemberRun(runId,memberId);
        return ResponseEntity.ok("참가완료");
    }

    //참가 취소
    @DeleteMapping("/{memberId}/no")
    public ResponseEntity<?> myCrewRunMemberNo(
//            @PathVariable("crewId") Long crewId,
                                           @PathVariable("runId") Long runId,
                                           @PathVariable("memberId") Long memberId){
        crewRunMemberService.deleteCrewMemberRun(runId,memberId);
        return ResponseEntity.ok("참가취소");
    }


    
}
