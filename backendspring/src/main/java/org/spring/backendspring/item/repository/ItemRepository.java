package org.spring.backendspring.item.repository;

import java.util.List;

import org.spring.backendspring.item.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    Page<ItemEntity> findByItemTitleContaining(Pageable pageable, String search);

    Page<ItemEntity> findByItemDetailContaining(Pageable pageable, String search);

    Page<ItemEntity> findByItemPriceContaining(Pageable pageable, String search);

    Page<ItemEntity> findByCategory(Pageable pageable, String search);

    public Page<ItemEntity> findByCategoryAndItemTitleContaining(Pageable pageable, String search, String subject, String search1);

    // for latest item..
    List<ItemEntity> findTop2ByOrderByCreateTimeDesc();

}
