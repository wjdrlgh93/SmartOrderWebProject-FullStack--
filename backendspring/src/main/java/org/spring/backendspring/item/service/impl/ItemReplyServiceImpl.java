package org.spring.backendspring.item.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.spring.backendspring.item.dto.ItemReplyDto;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemReplyEntity;
import org.spring.backendspring.item.repository.ItemReplyRepository;
import org.spring.backendspring.item.repository.ItemRepository;
import org.spring.backendspring.item.service.ItemReplyService;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemReplyServiceImpl implements ItemReplyService {

    private final ItemReplyRepository itemReplyRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertReply(ItemReplyDto itemReplyDto) {
        // ItemIdCheck 
        Optional<ItemEntity> optionalItemEntity 
                = itemRepository.findById(itemReplyDto.getItemId());
        if(optionalItemEntity.isPresent()){
            ItemEntity itemEntity = optionalItemEntity.get();
            itemReplyDto.setItemEntity(itemEntity);

        // memberCheck
        if(itemReplyDto.getMemberId() == null){
            throw new IllegalArgumentException("존재하지 않는회원");
        }
        Optional<MemberEntity> optionalMemberEntitiy 
            = memberRepository.findById(itemReplyDto.getMemberId());
        if(!optionalMemberEntitiy.isPresent()){
            throw new IllegalArgumentException("존재하지 않는회원입니다.");
        }
        itemReplyDto.setMemberEntity(optionalMemberEntitiy.get());

        ItemReplyEntity itemReplyEntity = 
                ItemReplyEntity.toReplyEntity(itemReplyDto);
        return itemReplyRepository.save(itemReplyEntity).getId();
    }
    return null;
}

    @Override
    public Page<ItemReplyDto> getReplyPage(Long itemId, Pageable pageable) {

        Page<ItemReplyEntity> replyentititesPage = 
            itemReplyRepository.findAllByItemEntity_id(itemId, pageable);

        Page<ItemReplyDto> replyPage = replyentititesPage
                .map(ItemReplyDto::toItemReplyDto);
            
        return replyPage;


    }

    @Override
    public void update(ItemReplyDto itemReplyDto) throws IOException {

        Optional<ItemReplyEntity>opEntity 
                = itemReplyRepository.findById(itemReplyDto.getId());
        if (opEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 댓글 ID 입니다");
        }
        ItemReplyEntity itemReplyEntity = opEntity.get();
        if (!itemReplyEntity.getMemberEntity().getId().equals(itemReplyDto.getMemberId())) {
            // Controller -> 403 ::  RuntimeException 사용 권장
            throw new IllegalAccessError("댓글 수정 권한이 없습니다. (작성자만 수정 가능)");

        }
        // Board ValidCheck
        Optional<ItemEntity> optionalItemEntity 
                = itemRepository.findById(itemReplyDto.getItemId());
        if (optionalItemEntity.isEmpty()) {
            throw new IllegalArgumentException("댓글의 게시글이 존재하지 않습니다.");
        }

        itemReplyDto.setItemEntity(optionalItemEntity.get());

        if (itemReplyDto.getMemberId() == null) {
            throw new IllegalArgumentException("회원 정보가 필요합니다.");
        }
        Optional<MemberEntity> optionalMemberEntity
                = memberRepository.findById(itemReplyDto.getMemberId());
        if (optionalMemberEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원 ID입니다.");
        }

        itemReplyDto.setMemberEntity(optionalMemberEntity.get());

        itemReplyEntity.updateFromDto(itemReplyDto);
        itemReplyRepository.save(itemReplyEntity);

    }

    @Override
    public void deleteReply(Long replyId, Long memberId) {

        if (replyId == null) {
            throw new IllegalArgumentException("삭제할 댓글 ID 가 필요합니다");
        }
        Optional<ItemReplyEntity> optionalItemReplyEntity 
                = itemReplyRepository.findById(replyId);
        if (optionalItemReplyEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 댓글 ID입니다.");
        }
        ItemReplyEntity itemReplyEntity = optionalItemReplyEntity.get();
        if (memberId == null) {
            throw new IllegalArgumentException("회원 정보가 필요합니다.");
        }
        if (!itemReplyEntity.getMemberEntity().getId().equals(memberId)) {
            throw new IllegalAccessError("댓글 삭제 권한이 없습니다. (작성자만 삭제 가능)");
        }
        itemReplyRepository.delete(itemReplyEntity);
    }
    
}
