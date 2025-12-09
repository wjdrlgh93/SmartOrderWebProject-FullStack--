package org.spring.backendspring.admin.controller;

import org.spring.backendspring.admin.dto.SummaryDto;
import org.spring.backendspring.admin.service.AdminSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/summary")
@RequiredArgsConstructor
public class AdminSummaryController {
    
    private final AdminSummaryService adminSummaryService;

    @GetMapping
    public ResponseEntity<SummaryDto> getSummary(){
        SummaryDto summaryDto = adminSummaryService.getSummary();
        return ResponseEntity.ok(summaryDto);
    }
    

}
