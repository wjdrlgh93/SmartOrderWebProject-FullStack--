package org.spring.backendspring.item.service;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {


    Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search);

    ItemDto itemDetail(Long itemId) throws IOException;


    Page<ItemDto> getItemsByCategory(Pageable pageable, String category, String subject, String search);



    List<ItemDto> getRecentItem();
}
