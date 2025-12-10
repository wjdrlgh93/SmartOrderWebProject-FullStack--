package org.spring.backendspring.admin.service;

import java.io.IOException;

import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.item.dto.ItemDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminItemService {

    ItemDto findById(Long id);

    void insertItem(ItemDto itemDto, MultipartFile itemFile, Long memberId);

    void update(ItemDto itemDto) throws IOException;

    
    void deleteItem(Long itemId);

    PagedResponse<ItemDto> findAllItems(String keyword, int page, int size);

    void deleteImage(Long id);
}
