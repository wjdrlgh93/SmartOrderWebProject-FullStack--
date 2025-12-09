package org.spring.backendspring.API.marathonapi.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.API.marathonapi.entity.Marathon;
import org.spring.backendspring.API.marathonapi.service.MarathonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MarathonController {

    private final MarathonService marathonService;

    // 수정된 MarathonController.java
    @GetMapping({ "/marathons" })
    public Page<Marathon> marathonList(
            // 파라미터 이름 ("searchTerm")을 명시하여 리플렉션 오류를 회피
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @PageableDefault(size = 10) Pageable pageable) {
        return marathonService.findMarathons(searchTerm, pageable);
    }
}