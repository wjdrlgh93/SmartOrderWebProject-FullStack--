package org.spring.backendspring.rabbitmqWebsocket.chat.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.rabbitmqWebsocket.chat.entity.MyCrewChatMessageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MyCrewChatMessageDto {
    
    private Long id;
    private Long crewId;
    private String crewName;
    private Long senderId;
    private String senderNickName;
    private String senderProfileUrl;
    private String message;
    private ChatMessageType type;
    private LocalDateTime createTime;

    public static MyCrewChatMessageDto toDto(MyCrewChatMessageEntity entity) {
        
        return MyCrewChatMessageDto.builder()
                .id(entity.getId())
                .crewId(entity.getCrewId())
                .crewName(entity.getCrewName())
                .senderId(entity.getSenderId())
                // .senderNickName(entity.getSenderNickName())
                // .senderProfileUrl(entity.getSenderProfileUrl())
                .message(entity.getMessage())
                .createTime(entity.getCreateTime())
                .type(entity.getType())
                .build();
    }
    public static MyCrewChatMessageDto toDto2(MyCrewChatMessageEntity entity, String nickName, String profileUrl) {
        
        return MyCrewChatMessageDto.builder()
                .id(entity.getId())
                .crewId(entity.getCrewId())
                .crewName(entity.getCrewName())
                .senderId(entity.getSenderId())
                .senderNickName(nickName)
                .senderProfileUrl(profileUrl)
                .message(entity.getMessage())
                .createTime(entity.getCreateTime())
                .type(entity.getType())
                .build();
    }
}
