package org.spring.backendspring.event.repository;

import org.spring.backendspring.event.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Page<EventEntity> findByEventTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
