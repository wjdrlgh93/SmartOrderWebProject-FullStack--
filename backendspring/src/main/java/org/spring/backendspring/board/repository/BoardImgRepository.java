package org.spring.backendspring.board.repository;

import java.util.Optional;

import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImgRepository extends JpaRepository<BoardImgEntity, Long>
{

    Optional<BoardImgEntity> findByBoardEntity(BoardEntity boardEntity);
    
}
