package org.spring.backendspring.admin.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.spring.backendspring.admin.repository.AdminItemRepository;
import org.spring.backendspring.admin.service.AdminItemService;
import org.spring.backendspring.board.dto.BoardDto;
import org.spring.backendspring.board.dto.BoardImgDto;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.item.dto.ItemDto;
import org.spring.backendspring.item.dto.ItemImgDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;
import org.spring.backendspring.item.repository.ItemImgRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminItemServiceImpl implements AdminItemService {

    private final ItemRepository itemRepository;
    private final AdminItemRepository adminItemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;

    private final AwsS3Service awsS3Service;

    @Value("${s3file.path.item}")
    private String path;

    // ===========================================================
    // FIND ONE
    // ===========================================================
    @Override
    public ItemDto findById(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::toItemDto)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
    }

    // ===========================================================
    // INSERT
    // ===========================================================
    @Override
    @Transactional
    public void insertItem(ItemDto itemDto, MultipartFile itemFile, Long memberId) {

        String newFileName = null;
        String fileUrl = null;
        String originalName = null;

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버 없음"));

        ItemEntity item = ItemEntity.builder()
                .itemTitle(itemDto.getItemTitle())
                .itemDetail(itemDto.getItemDetail())
                .itemPrice(itemDto.getItemPrice())
                .itemSize(itemDto.getItemSize())
                .category(itemDto.getCategory())
                .attachFile(0)
                .memberEntity(member)
                .build();

        if (itemFile != null && !itemFile.isEmpty()) {

            originalName = itemFile.getOriginalFilename();

            try {
                newFileName = awsS3Service.uploadFile(itemFile, path);
                fileUrl = awsS3Service.getFileUrl(newFileName);

            } catch (IOException e) {
                // TODO: handle exception
                throw new RuntimeException("S3 파일 업로드 및 URL 획득 실패", e);
            }
            item.setAttachFile(1);
        }


        ItemEntity savedItem = itemRepository.save(item);

        // save Img table
        if (newFileName != null) {
            itemImgRepository.save(
                    ItemImgEntity.builder()
                            .itemEntity(savedItem)
                            .oldName(originalName)
                            .newName(newFileName)
                            .build());
        }
    }

    // ===========================================================
    // UPDATE
    // ===========================================================

    @Transactional
    @Override
    public void update(ItemDto itemDto) throws IOException {

        ItemEntity itemEntity = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 상품이 없습니다"));

        String newFileName = null;
        String newFileUrl = null;
        String originalName = null;

        // if there is NewFile..
        if (itemDto.getItemFile() != null && !itemDto.getItemFile().isEmpty()) {

            MultipartFile newFile = itemDto.getItemFile();
            Optional<ItemImgEntity> existImgOptional = itemImgRepository.findByItemEntity(itemEntity);

            if (existImgOptional.isPresent()) {
                ItemImgEntity existFileEntity = existImgOptional.get();
                awsS3Service.deleteFile(existFileEntity.getNewName());
                itemImgRepository.delete(existFileEntity);
            }
            String originalFileName = newFile.getOriginalFilename();
            newFileName = awsS3Service.uploadFile(newFile, "");

            itemEntity.setAttachFile(1);
            ItemImgEntity newFileEntity = ItemImgEntity.toItemImgEntity(ItemImgDto.builder()
                    .oldName(originalFileName)
                    .newName(newFileName)
                    .itemEntity(itemEntity)
                    .build());
            itemImgRepository.save(newFileEntity);
        }
        itemRepository.save(itemEntity);


    }

    // ===========================================================
    // DELETE
    // ===========================================================
    @Override
    @Transactional
    public void deleteItem(Long itemId) {

        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("해당 Item이 존재하지 않습니다."));

        Optional<ItemImgEntity> imgOptional = itemImgRepository.findByItemEntityId(itemId);

        if (imgOptional.isPresent()) {
            ItemImgEntity itemImg = imgOptional.get();
            String newFileName = itemImg.getNewName();

            try {
                awsS3Service.deleteFile(newFileName);
            } catch (Exception e) {
                System.err.println("S3 파일 삭제 중 오류 발생 (Key: " + newFileName + "): " + e.getMessage());
            }
            itemImgRepository.delete(itemImg);
        }
        itemRepository.delete(item);

    }

    // ===========================================================
    // FIND ALL
    // ===========================================================
    @Override
    public PagedResponse<ItemDto> findAllItems(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ItemDto> itemPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            itemPage = itemRepository.findAll(pageable).map(ItemDto::toItemDto);
        } else {
            itemPage = adminItemRepository
                    .findByItemTitleContainingIgnoreCaseOrItemDetailContainingIgnoreCase(keyword, keyword, pageable)
                    .map(ItemDto::toItemDto);
        }

        return PagedResponse.of(itemPage);
    }

    @Override
    public void deleteImage(Long id) {
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품 없음"));
        item.setAttachFile(0);
        itemRepository.save(item);
    }
}

// package org.spring.backendspring.admin.service.impl;

// import java.io.File;
// import java.io.IOException;
// import java.util.UUID;

// import org.spring.backendspring.admin.repository.AdminItemRepository;
// import org.spring.backendspring.admin.service.AdminItemService;
// import org.spring.backendspring.common.dto.PagedResponse;
// import org.spring.backendspring.item.dto.ItemDto;
// import org.spring.backendspring.item.entity.ItemEntity;
// import org.spring.backendspring.item.entity.ItemImgEntity;
// import org.spring.backendspring.item.repository.ItemImgRepository;
// import org.spring.backendspring.item.repository.ItemRepository;
// import org.spring.backendspring.member.dto.MemberDto;
// import org.spring.backendspring.member.entity.MemberEntity;
// import org.spring.backendspring.member.repository.MemberRepository;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import jakarta.persistence.EntityNotFoundException;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class AdminItemServiceImpl implements AdminItemService {

// private final ItemRepository itemRepository;
// private final AdminItemRepository adminItemRepository;
// private final ItemImgRepository itemImgRepository;
// private final MemberRepository memberRepository;

// @Override
// public ItemDto findById(Long id) {
// return itemRepository.findById(id)
// .map(entity -> ItemDto.toItemDto(entity))
// .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
// }

// @Override
// public void insertItem(ItemDto itemDto, MultipartFile itemFile, Long
// memberId) {

// MemberEntity member = memberRepository.findById(memberId)
// .orElseThrow(() -> new RuntimeException("멤버 없음"));

// ItemEntity item = ItemEntity.toItemEntity(itemDto);

// item.setAttachFile(0); // 기본값 파일 이미지 없음

// item.setMemberEntity(member);

// if (itemFile == null || itemFile.isEmpty()) {
// return;
// }

// String uploadPath = "E:\\uploadImg\\";
// File folder = new File(uploadPath);

// if (!folder.exists()) {
// folder.mkdirs(); // 폴더가 없으면 자동 생성
// }

// String originalName = itemFile.getOriginalFilename();
// String newName = UUID.randomUUID() + "_" + originalName;

// try {
// itemFile.transferTo(new File(uploadPath + newName));
// } catch (IOException e) {
// throw new RuntimeException("파일 저장 실패", e);
// }

// // 6) 이미지 엔티티 저장
// ItemImgEntity img = ItemImgEntity.builder()
// .itemEntity(item)
// .oldName(originalName)
// .newName(newName)
// .build();

// itemRepository.save(item);

// item.setAttachFile(1);

// itemImgRepository.save(img);

// // 7) 첨부파일 상태 업데이트

// }

// @Override
// public ItemDto updateItem(Long id, ItemDto updatedDto, MultipartFile
// itemFile, Long memberId) {
// ItemEntity existingItem = itemRepository.findById(id)
// .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
// // 업데이트 필드 설정
// MemberEntity member = memberRepository.findById(memberId)
// .orElseThrow(() -> new RuntimeException("멤버 없음"));

// existingItem.setMemberEntity(member);

// ItemImgEntity oldImg = itemImgRepository.findByItemEntity(existingItem);
// String uploadPath = "E:\\uploadImg\\";

// if (updatedDto.getAttachFile() == 0) {
// // 기존 이미지 파일 삭제
// if (oldImg != null) {
// new File(uploadPath + oldImg.getNewName()).delete();
// itemImgRepository.delete(oldImg);
// }

// ItemEntity updatedEntity = ItemEntity.builder()
// .id(existingItem.getId())
// .itemTitle(updatedDto.getItemTitle())
// .itemDetail(updatedDto.getItemDetail())
// .itemPrice(updatedDto.getItemPrice())
// .itemSize(updatedDto.getItemSize())
// .attachFile(updatedDto.getAttachFile())
// .build();

// return ItemDto.toItemDto(itemRepository.save(updatedEntity));
// }
// // 이미지 파일이 있는 경우
// if (itemFile != null && !itemFile.isEmpty()) {
// // 기존 이미지 파일 삭제
// if (oldImg != null) {
// new File(uploadPath + oldImg.getNewName()).delete();
// itemImgRepository.delete(oldImg);
// }
// File folder = new File(uploadPath);
// if (!folder.exists())
// folder.mkdirs();

// String originalName = itemFile.getOriginalFilename();
// String newName = UUID.randomUUID() + "_" + originalName;

// try {
// itemFile.transferTo(new File(uploadPath + newName));
// } catch (Exception e) {
// throw new RuntimeException("파일 저장 실패", e);
// }

// // 새 이미지 저장
// itemImgRepository.save(
// ItemImgEntity.builder()
// .itemEntity(existingItem)
// .oldName(originalName)
// .newName(newName)
// .build());

// ItemEntity updatedEntity = ItemEntity.builder()
// .id(existingItem.getId())
// .itemTitle(updatedDto.getItemTitle())
// .itemDetail(updatedDto.getItemDetail())
// .itemPrice(updatedDto.getItemPrice())
// .itemSize(updatedDto.getItemSize())
// .attachFile(1)
// .build();

// return ItemDto.toItemDto(itemRepository.save(updatedEntity));
// }

// ItemEntity updatedEntity = ItemEntity.builder()
// .id(existingItem.getId())
// .itemTitle(updatedDto.getItemTitle())
// .itemDetail(updatedDto.getItemDetail())
// .itemPrice(updatedDto.getItemPrice())
// .itemSize(updatedDto.getItemSize())
// .attachFile(existingItem.getAttachFile()) // 그대로 유지
// .build();

// return ItemDto.toItemDto(itemRepository.save(updatedEntity));

// }

// @Override
// public void deleteItem(Long id) {
// ItemEntity item = itemRepository.findById(id)
// .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다"));
// itemRepository.delete(item);
// }

// @Override
// public PagedResponse<ItemDto> findAllItems(String keyword, int page, int
// size) {
// Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
// Page<ItemDto> itemPage;

// if (keyword == null || keyword.trim().isEmpty()) {
// itemPage = itemRepository.findAll(pageable)
// .map(ItemDto::toItemDto);
// } else {
// itemPage = adminItemRepository
// .findByItemTitleContainingIgnoreCaseOrItemDetailContainingIgnoreCase(keyword,
// keyword, pageable)
// .map(ItemDto::toItemDto);
// }

// return PagedResponse.of(itemPage);
// }
// }