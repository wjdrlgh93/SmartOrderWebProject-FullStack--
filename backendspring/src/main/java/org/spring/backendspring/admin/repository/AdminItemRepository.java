package org.spring.backendspring.admin.repository;

import org.spring.backendspring.item.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminItemRepository extends JpaRepository<ItemEntity, Long> {

    // search 용도
    Page<ItemEntity> findByItemTitleContainingIgnoreCaseOrItemDetailContainingIgnoreCase(
            String titleKeyword,
            String detailKeyword,
            Pageable pageable);
}
