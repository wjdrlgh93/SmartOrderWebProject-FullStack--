package org.spring.backendspring.member.controller;

import org.spring.backendspring.member.service.MemberProfileImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profileImage")
@RequiredArgsConstructor
public class MemberProfileImageController {

    private final MemberProfileImageService memberProfileImageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file,
            @RequestParam("memberId") Long memberId) {
        try {
            memberProfileImageService.uploadProfileImage(file, memberId);
            return ResponseEntity.ok("프로필 이미지 업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 이미지 업로드 실패: " + e.getMessage());
        }
    }
}
