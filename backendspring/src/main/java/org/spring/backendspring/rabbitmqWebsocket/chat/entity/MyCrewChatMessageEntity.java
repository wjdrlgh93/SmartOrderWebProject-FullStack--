package org.spring.backendspring.rabbitmqWebsocket.chat.entity;

import java.time.LocalDateTime;

import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.MyCrewChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_message_tb")
public class MyCrewChatMessageEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    
    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    private String crewName;

    // private String senderNickName;

    // private String senderProfileUrl;

    private String message;

    @Enumerated(EnumType.STRING)
    private ChatMessageType type;

    private LocalDateTime createTime;

    public static MyCrewChatMessageEntity toEntity(MyCrewChatMessageDto dto) {

        return MyCrewChatMessageEntity.builder()
                .senderId(dto.getSenderId())
                .crewId(dto.getCrewId())
                .crewName(dto.getCrewName())
                // .senderNickName(dto.getSenderNickName())
                // .senderProfileUrl(dto.getSenderProfileUrl())
                .message(dto.getMessage())
                .type(dto.getType())
                .createTime(dto.getCreateTime())
                .build();
    }
}
