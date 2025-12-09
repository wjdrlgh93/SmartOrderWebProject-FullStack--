package org.spring.backendspring.item.repository;

import org.spring.backendspring.item.entity.ItemReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemReplyRepository extends JpaRepository<ItemReplyEntity, Long> {

    Page<ItemReplyEntity> findAllByItemEntity_id(Long itemId, Pageable pageable);
    
}
