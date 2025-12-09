package org.spring.backendspring.rabbitmqWebsocket.chat.repository;

import java.util.List;

import org.spring.backendspring.rabbitmqWebsocket.chat.entity.MyCrewChatMessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<MyCrewChatMessageEntity, Long> {

    long countByCrewId(Long crewId);

    List<MyCrewChatMessageEntity> findByCrewId(Long crewId, Pageable pageable);

    List<MyCrewChatMessageEntity> findByCrewIdOrderByCreateTimeDesc(Long crewId, Pageable pageable);
    
}
