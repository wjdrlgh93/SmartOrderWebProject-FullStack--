package org.spring.backendspring.member.controller;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/email/check")
    public ResponseEntity<?> getEmailCheck(@RequestParam("email") String userEmail) {

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일은 필수 입력 사항입니다.");
        } else if (!userEmail.matches((emailRegex))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 이메일입니다.");
        }

        int num = memberService.userEmailCheck(userEmail);
        if (num == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        } else if (num == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("탈퇴한 이메일은 재가입할 수 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일입니다.");
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestPart("memberDto") MemberDto memberDto,
                                  BindingResult bindingResult,
                                  @RequestPart(value = "memberFile", required = false) MultipartFile memberFile) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, Object> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(err -> {
                    errors.put(err.getField(), err.getDefaultMessage());
                });
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            memberService.insertMember(memberDto, memberFile);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getMember(@AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        MemberDto memberDetail = memberService.findById(myUserDetails.getMemberId());
        return ResponseEntity.ok(memberDetail);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMember(@RequestPart("memberDto") MemberDto memberDto,
                                          @RequestPart(value = "memberFile", required = false) MultipartFile memberFile,
                                          @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        MemberDto updated = memberService.updateMember(myUserDetails.getMemberId(), memberDto, memberFile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        boolean roleAdmin = myUserDetails.getAuthorities().stream()
                .anyMatch(el -> el.getAuthority().equals("ROLE_ADMIN"));
        if (roleAdmin) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ADMIN 권한은 탈퇴할 수 없습니다.");
        }
        memberService.deleteMember(myUserDetails.getMemberId());
        return ResponseEntity.ok("회원 탈퇴 확인");
    }
}
