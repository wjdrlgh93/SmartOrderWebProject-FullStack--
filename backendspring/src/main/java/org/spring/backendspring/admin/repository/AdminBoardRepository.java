package org.spring.backendspring.admin.repository;

import org.spring.backendspring.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminBoardRepository extends JpaRepository<BoardEntity, Long> {

    // search 용도
    Page<BoardEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable);

    Page<BoardEntity> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<BoardEntity> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT COUNT(b) FROM BoardEntity b")
    long countAll();

    @Query("SELECT COUNT(b) FROM BoardEntity b WHERE DATE(b.createTime) = CURRENT_DATE")
    long countToday();

}