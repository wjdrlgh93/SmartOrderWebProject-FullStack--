package org.spring.backendspring.crew.crewCreate.controller;

import java.util.Map;

import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;
import org.spring.backendspring.crew.crewCreate.service.CrewCreateRequestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/crew/create/request")
@RequiredArgsConstructor
public class CrewCreateRequestController {
    
    private final CrewCreateRequestService createRequestService;

    @PostMapping({"/", ""})
    public ResponseEntity<?> createRequest(@Valid @RequestBody CrewCreateRequestDto crewCreateRequestDto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal MyUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("message", "입력 오류"));
        }

        Long loginUserId = userDetails.getMemberId();
        // crewCreateRequestDto.setMemberId(memberId);
        createRequestService.createRequest(crewCreateRequestDto, loginUserId);
        crewCreateRequestDto.setMemberId(loginUserId);

        return ResponseEntity.ok().body(Map.of("message", "크루 신청 성공"));

    }

    @PostMapping("/approved")
    public ResponseEntity<?> approveRequest(@RequestParam("requestId") Long requestId) {

        createRequestService.approveRequest(requestId);

        return ResponseEntity.ok().body(Map.of
                        ("message", "크루 신청 승인 완료", "requestId", requestId));
    }

    @PostMapping("/rejected")
    public ResponseEntity<?> rejectRequest(@RequestParam("requestId") Long requestId) {

        createRequestService.rejectRequest(requestId);

        return ResponseEntity.ok().body(Map.of
                        ("message", "크루 신청 거절 완료", "requestId", requestId));
    }
}
