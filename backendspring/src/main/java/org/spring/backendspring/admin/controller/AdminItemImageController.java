package org.spring.backendspring.admin.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.admin.service.AdminItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/item/image")
public class AdminItemImageController {

    private final AdminItemService adminItemService;

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long itemId) {
        adminItemService.deleteImage(itemId);
        return ResponseEntity.ok("이미지 삭제 완료");
    }
}
