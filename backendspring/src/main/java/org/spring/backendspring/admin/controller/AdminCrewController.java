package org.spring.backendspring.admin.controller;

import org.spring.backendspring.admin.repository.AdminCrewRepository;
import org.spring.backendspring.admin.service.AdminCrewService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crew.dto.CrewDto;
import org.spring.backendspring.crew.crew.service.CrewService;
import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;
import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;
import org.spring.backendspring.member.dto.MemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/crew")
public class AdminCrewController {

    private final CrewService crewService;
    private final AdminCrewService adminCrewService;
    private final AdminCrewRepository adminCrewRepository;

    @GetMapping("/crewList")
    public ResponseEntity<PagedResponse<CrewDto>> getAllCrews(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<CrewDto> crewList = adminCrewService.findAllCrews(keyword, page, size);
        return ResponseEntity.ok(crewList);
    }

    @GetMapping("/detail/{crewId}")
    public ResponseEntity<?> getCrewDetail(@PathVariable("crewId") Long crewId) {
        CrewDto crewDto = adminCrewService.findByDetailCrew(crewId);
        return ResponseEntity.ok(crewDto);
    }

    @GetMapping("/create/requestList")
    public ResponseEntity<?> getCreateRequestList(@RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        PagedResponse<CrewCreateRequestDto> crewCreateRequest =
                adminCrewService.findAllCrewCreateRequest(keyword, page, size);
        return ResponseEntity.ok(crewCreateRequest);
    }

    @GetMapping("/create/detail/{crewRequestId}")
    public ResponseEntity<?> getCreateRequestDetail(@PathVariable("crewRequestId") Long crewRequestId) {
        CrewCreateRequestDto crewDto = adminCrewService.crewRequestDetail(crewRequestId);
        return ResponseEntity.ok(crewDto);
    }

    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<String> deleteCrew(@PathVariable("crewId") Long id) {
        adminCrewService.deleteCrewByAdmin(id);
        return ResponseEntity.ok("관리자에 의해 크루가 삭제 되었습니다");
    }

    @GetMapping("/total")
    public long getTotalCrews() {
        return adminCrewRepository.countAll();
    }

    @GetMapping("/today")
    public long getTodayCrews() {
        return adminCrewRepository.countToday();
    }
}
