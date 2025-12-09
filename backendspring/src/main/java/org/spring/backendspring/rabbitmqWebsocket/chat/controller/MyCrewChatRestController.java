package org.spring.backendspring.rabbitmqWebsocket.chat.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.config.security.MyUserDetails;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.MyCrewChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageType;
import org.spring.backendspring.rabbitmqWebsocket.chat.webSocketService.MyCrewChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RestController
@RequestMapping("/api/mycrew/{crewId}/chat")
@RequiredArgsConstructor
public class MyCrewChatRestController {
    
    private final MyCrewChatService crewChatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final CrewMemberRepository crewMemberRepository;
    private final AwsS3Service awsS3Service;


    @GetMapping("/recent")
    public List<MyCrewChatMessageDto> recentMessages(@PathVariable("crewId") Long crewId,
                                                     @RequestParam(name = "limit", defaultValue = "300") int limit) {
        return crewChatService.recentMessages(crewId, limit);
    }

    @PostMapping("/send")
    public MyCrewChatMessageDto sendmessage(@RequestBody MyCrewChatMessageDto dto,
                                            @PathVariable Long crewId,
                                            @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {
        CrewMemberEntity crewMemberEntity = crewMemberRepository.findById(userDetails.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루 멤버"));
        CrewMemberDto crewMember = CrewMemberDto.toCrewChatMember(crewMemberEntity, awsS3Service);
        dto.setCrewId(crewId);
        dto.setSenderId(userDetails.getMemberId());
        dto.setSenderNickName(userDetails.getNickName());
        dto.setType(ChatMessageType.CHAT);
        dto.setCreateTime(LocalDateTime.now());
        dto.setSenderProfileUrl(crewMember.getFileUrl().get(0));
        log.info("%%%%"+crewMember.getFileUrl().get(0));
        return crewChatService.saveMessage(dto);
    } 
                                     
                                            
    // @GetMapping("/participants")
    // public CrewChatParticipantsDto getCount(@PathVariable Long crewId) {
    //     int count = crewChatService.getActiveCount(crewId);
    //     return new CrewChatParticipantsDto(count);
    // }
}
