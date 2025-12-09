package org.spring.backendspring.admin.repository;

import org.spring.backendspring.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEventRepository extends JpaRepository<EventEntity, Long> {

    // search 용도
    Page<EventEntity> findByEventTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
