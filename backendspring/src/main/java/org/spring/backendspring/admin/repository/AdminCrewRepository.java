package org.spring.backendspring.admin.repository;

import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminCrewRepository extends JpaRepository<CrewEntity, Long> {

    // search 용도
    Page<CrewEntity> findByNameContainingIgnoreCase(String nameKeyword, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CrewEntity c")
    long countAll();

    @Query("SELECT COUNT(c) FROM CrewEntity c WHERE DATE(c.createTime) = CURRENT_DATE")
    long countToday();
}