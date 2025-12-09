package org.spring.backendspring.crew.crewMember.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.service.CrewMemberService;
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
@RequestMapping("/api/mycrew/{crewId}/member")
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

    //크루원 리스트
    @GetMapping({"","/"})
    public ResponseEntity<?> CrewMemberList(@PathVariable("crewId") Long crewId,
            @PageableDefault(page = 0, size = 8, sort = "id")Pageable pageable,
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "search", required = false) String search){
        Page<CrewMemberDto> page = crewMemberService.findCrewMemberList(crewId, pageable, subject, search);
        int blockNum = 3;
        int nowPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();

        int startPage = ((nowPage - 1) / blockNum) * blockNum + 1;
        int endPage = Math.min(startPage + blockNum - 1, totalPages);
        
        Map<String, Object> crewMemberListMap = new HashMap<>();
        crewMemberListMap.put("crewMember", page);
        crewMemberListMap.put("nowPage", nowPage);
        crewMemberListMap.put("totalPages", totalPages);
        crewMemberListMap.put("startPage", startPage);
        crewMemberListMap.put("endPage", endPage);
        crewMemberListMap.put("pageSize", page.getSize());
        crewMemberListMap.put("totalElements", page.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(crewMemberListMap);
    }

    //크루원 상세보기
    @GetMapping("detail/{crewMemberId}")
    public ResponseEntity<?> crewMemberDetails(@PathVariable("crewId") Long crewId,
                                               @PathVariable("crewMemberId") Long crewMemberId){
        CrewMemberDto crewMemberDto = crewMemberService.detailCrewMember(crewId,crewMemberId);
        Map<String, Object> crewMemberDetail = new HashMap<>();
        crewMemberDetail.put("crewMember", crewMemberDto);
        return ResponseEntity.status(HttpStatus.OK).body(crewMemberDetail);
    }

    //크루원 삭제 (탈퇴?)
    @GetMapping("delete/{crewMemberTbId}")
    public ResponseEntity<?> crewMemberDelete(@PathVariable("crewId") Long crewId,
                                               @PathVariable("crewMemberTbId") Long crewMemberTbId){
        crewMemberService.deleteCrewMember(crewMemberTbId);
        return ResponseEntity.ok("크루원삭제완료");
    }
}
