package org.spring.backendspring.rabbitmqWebsocket.chat.webSocketService;

import java.io.IOException;
import java.util.List;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.MyCrewChatMessageDto;

public interface MyCrewChatService {

    MyCrewChatMessageDto saveMessage(MyCrewChatMessageDto message) throws IOException;

    List<MyCrewChatMessageDto> recentMessages(Long crewId, int limit);

    MyCrewChatMessageDto enterChat(Long crewId, Long memberId) throws IOException;

    MyCrewChatMessageDto leaveChat(Long crewId, Long memberId) throws IOException;

    int getActiveCount(Long crewId);
    
}
