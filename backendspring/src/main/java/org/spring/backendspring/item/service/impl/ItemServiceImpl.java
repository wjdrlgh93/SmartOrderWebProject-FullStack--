package org.spring.backendspring.item.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemImgRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    // Bean Injection
    private final ItemRepository itemRepository;
    private final ItemImgRepository imgRepository;
    private final AwsS3Service awsS3Service;


    @Override
    public Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search) {

        // init 
        Page<ItemEntity> itemEntities = null;

        if (subject == null || search == null || search.equals("")) {
            itemEntities = itemRepository.findAll(pageable);
        } else {
            if (subject.equals("itemTitle")) {
                itemEntities = itemRepository.findByItemTitleContaining(pageable, search);
            } else if (subject.equals("itemDetail")) {
                itemEntities = itemRepository.findByItemDetailContaining(pageable, search);
            } else if (subject.equals("itemPrice")) {
                itemEntities = itemRepository.findByItemPriceContaining(pageable, search);
            } else {
                itemEntities = itemRepository.findAll(pageable);
            }
        }
        return itemEntities.map(dto -> {
            ItemDto itemDto = ItemDto.toItemDto(dto);
            if (itemDto.getAttachFile() == 1) {
                try {
                    itemDto.setFileUrl(awsS3Service.getFileUrl(itemDto.getItemImgDtos().get(0).getNewName()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return itemDto;
        });

    }

    // category Paging
    @Override
    public Page<ItemDto> getItemsByCategory(Pageable pageable, String category, String subject, String search) {

        Page<ItemEntity> itemEntities = null;
        if (search != null && !search.isEmpty() && "itemTitle".equals(subject)) {
            itemEntities = itemRepository.findByCategoryAndItemTitleContaining(pageable, category, subject, search);
        } else {
            itemEntities = itemRepository.findByCategory(pageable, category);
        }
        return itemEntities.map(dto -> {
            ItemDto itemDto = ItemDto.toItemDto(dto);
            if (itemDto.getAttachFile() == 1) {
                try {
                    itemDto.setFileUrl(awsS3Service.getFileUrl(itemDto.getItemImgDtos().get(0).getNewName()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return itemDto;
        });
    }

    @Override
    public ItemDto itemDetail(Long itemId) throws IOException {
        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("상품에 해당하는 아이디가 존재하지 않음"));
        ItemDto itemDto = ItemDto.toItemDto(itemEntity);
        if (itemDto.getAttachFile() == 1) {
            itemDto.setFileUrl(awsS3Service.getFileUrl(itemEntity.getItemImgEntities().get(0).getNewName()));
        }
        return itemDto;
    }

    // this method for Show recentlyItem.
    @Override
    public List<ItemDto> getRecentItem() {
        List<ItemEntity> recentlyentities = itemRepository.findTop2ByOrderByCreateTimeDesc();

        return recentlyentities.stream()
                .map(dto -> {
                    ItemDto itemDto = ItemDto.toItemDto(dto);
                    if (itemDto.getAttachFile() == 1) {
                        try {
                            itemDto.setFileUrl(awsS3Service.getFileUrl(itemDto.getItemImgDtos().get(0).getNewName()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return itemDto;
                }).collect(Collectors.toList());
    }
}
