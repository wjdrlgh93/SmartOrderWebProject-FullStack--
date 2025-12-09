package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import org.spring.backendspring.rabbitmqWebsocket.chat.dto.BotMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmqService.MyCrewBotService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
@RequiredArgsConstructor
public class MyCrewBotController {
    
    private final MyCrewBotService myCrewBotService;

    @MessageMapping("/chatBot")
    public void sendBotMessage(BotMessageDto botMessageDto) {
        log.info("================채팅 컨트롤러까지옴 ================");
        myCrewBotService.sendCrewBot(botMessageDto);
    }

}
