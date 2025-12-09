package org.spring.backendspring.item.repository;

import java.util.Optional;

import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImgEntity, Long> {

    Optional<ItemImgEntity> findByItemEntity(ItemEntity existingItem);

    Optional<ItemImgEntity> findByItemEntityId(Long itemId);
    
}
