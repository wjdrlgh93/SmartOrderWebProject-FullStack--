package org.spring.backendspring.admin.controller;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.admin.dto.AdminMemberDto;
import org.spring.backendspring.admin.repository.AdminMemberRepository;
import org.spring.backendspring.admin.service.AdminMemberService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final MemberService memberService;
    private final AdminMemberService adminMemberService;
    private final AdminMemberRepository adminMemberRepository;

    @GetMapping({ "", "/", "/index" })
    
    public ResponseEntity<?> adminIndex() {
        
        
        
        return ResponseEntity.ok("관리자 페이지에 접근했습니다.");
    }

    @GetMapping("/member/detail/{id}")
    
    public ResponseEntity<MemberDto> getMember(@PathVariable("id") Long id) throws IOException {
        MemberDto memberDetail = memberService.findById(id);
        return ResponseEntity.ok(memberDetail);
    }

    
    @GetMapping("/member/memberList")
    public ResponseEntity<PagedResponse<MemberDto>> getAllMembers(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        PagedResponse<MemberDto> memberList = adminMemberService.findAllMembers(search, subject, page, size);
        return ResponseEntity.ok(memberList);
    }

    
    @PutMapping(value = "/member/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @RequestBody AdminMemberDto updatedDto) {
        adminMemberService.updateMemberByAdmin(id, updatedDto);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    @DeleteMapping("/member/delete/{id}")
    
    public ResponseEntity<String> adminDeleteMember(@PathVariable("id") Long id) {
        adminMemberService.deleteMemberByAdmin(id);
        return ResponseEntity.ok("관리자에 의해 탈퇴되었습니다.");
    }

    @GetMapping("/total")
    public long getTotalMembers() {
        return adminMemberRepository.countAll();
    }

    @GetMapping("/today")
    public long getTodayMembers() {
        return adminMemberRepository.countToday();
    }
}