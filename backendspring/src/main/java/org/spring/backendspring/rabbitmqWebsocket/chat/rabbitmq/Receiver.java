package org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmq;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.BotMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Receiver {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "${spring.rabbitmq.crew.queue}")
    public void receiveMessage(BotMessageDto botMessageDto) {

        String destination = "/topic/crewChatBot/" + botMessageDto.getCrewId() 
        + "/" + botMessageDto.getMemberId();

        messagingTemplate.convertAndSend(destination, botMessageDto);
    }
}
