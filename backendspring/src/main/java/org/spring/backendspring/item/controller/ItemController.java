package org.spring.backendspring.item.controller;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("")
    public ResponseEntity<Page<ItemDto>> itemSearchList(
            @PageableDefault(size = 9, direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search) {

        Page<ItemDto> itemList = itemService.pagingSearchItemList(pageable, subject, search);
        return ResponseEntity.ok(itemList);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ItemDto>> getRecentItems(){
        List<ItemDto> recentItems = itemService.getRecentItem();

        return ResponseEntity.ok(recentItems);

    }

    // URL: http://localhost:8088/api/shop/detail/{id}
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("id") Long id) throws IOException {

        // Bring Id(Long id ) = > item ID 
        // BoardDto boardDto = boardService.boardDetail(id);
        ItemDto itemDto = itemService.itemDetail(id);

        return ResponseEntity.ok(itemDto);
    }

    // item for Category

    @GetMapping("/shoes")
    public ResponseEntity<Page<ItemDto>> itemShoesList(
            @PageableDefault(size = 9, direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search
    ) {

        final String category = "shoes";
        Page<ItemDto> itemList = itemService.getItemsByCategory(pageable, category, subject, search);

        return ResponseEntity.ok(itemList);

    }
    
    @GetMapping("/equipment")
    public ResponseEntity<Page<ItemDto>> itemEquipmentList(
            @PageableDefault(size = 9, direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search
    ) {

        final String category = "equipment";
        Page<ItemDto> itemList = itemService.getItemsByCategory(pageable, category, subject, search);

        return ResponseEntity.ok(itemList);

    }
    
    @GetMapping("/accessory")
    public ResponseEntity<Page<ItemDto>> itemAccessoryList(
            @PageableDefault(size = 9, direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search
    ) {

        final String category = "accessory";
        Page<ItemDto> itemList = itemService.getItemsByCategory(pageable, category, subject, search);

        return ResponseEntity.ok(itemList);

    }
    
    @GetMapping("/cloth")
    public ResponseEntity<Page<ItemDto>> itemClothList(
            @PageableDefault(size = 9, direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search
    ) {

        final String category = "cloth";
        Page<ItemDto> itemList = itemService.getItemsByCategory(pageable, category, subject, search);

        return ResponseEntity.ok(itemList);

    }
    
    @GetMapping("/nutrition")
    public ResponseEntity<Page<ItemDto>> itemNutritionList(
            @PageableDefault(size = 9, direction = Sort.Direction.DESC, sort = "createTime") Pageable pageable,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "search", required = false) String search
    ) {

        final String category = "nutrition";
        Page<ItemDto> itemList
                = itemService.getItemsByCategory(pageable, category, subject, search);

        return ResponseEntity.ok(itemList);

    }

}
