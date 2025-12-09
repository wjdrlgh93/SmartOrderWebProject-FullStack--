package org.spring.backendspring.board.repository;


import java.util.List;

import java.util.Optional;
import org.spring.backendspring.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>
{

    @Query("SELECT b FROM BoardEntity b JOIN FETCH b.memberEntity")
    List<BoardEntity> findAllWithMember();

    Page<BoardEntity> findByTitleContaining(Pageable pageable, String search);

    Page<BoardEntity> findByContentContaining(Pageable pageable, String search);

    @Modifying // ⭐ 쿼리가 데이터를 변경(UPDATE, DELETE)함을 알림
    @Transactional // ⭐ UPDATE 쿼리는 트랜잭션 내에서 실행되어야 함
    @Query("UPDATE BoardEntity b SET b.hit = b.hit + 1 WHERE b.id = :id")
    void updateHit(@Param("id")Long id);
    
    Page<BoardEntity> findByCategoryAndTitleContaining(PageRequest request, String category, String keyword);

    Page<BoardEntity> findByCategory(PageRequest request, String category);

    Optional<BoardEntity> findByCategoryAndId(String category, Long noticeId);

    Page<BoardEntity> findByMemberEntity_NickNameContaining(Pageable pageable, String search);
}
