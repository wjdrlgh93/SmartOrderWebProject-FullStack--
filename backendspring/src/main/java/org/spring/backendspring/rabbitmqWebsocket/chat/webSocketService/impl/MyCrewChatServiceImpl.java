package org.spring.backendspring.rabbitmqWebsocket.chat.webSocketService.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.spring.backendspring.config.s3.AwsS3Service;
import org.spring.backendspring.crew.crewMember.dto.CrewMemberDto;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.MyCrewChatMessageDto;
import org.spring.backendspring.rabbitmqWebsocket.chat.dto.ChatMessageType;
import org.spring.backendspring.rabbitmqWebsocket.chat.entity.MyCrewChatMessageEntity;
import org.spring.backendspring.rabbitmqWebsocket.chat.repository.ChatMessageRepository;
import org.spring.backendspring.rabbitmqWebsocket.chat.webSocketService.MyCrewChatService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MyCrewChatServiceImpl implements MyCrewChatService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public MyCrewChatMessageDto saveMessage(MyCrewChatMessageDto message) throws IOException {
        CrewMemberEntity crewMember = crewMemberRepository.findByCrewEntityIdAndMemberEntityId(message.getCrewId(), message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 크루의 멤버가 아닙니다."));

        CrewMemberDto chatMember = CrewMemberDto.toCrewChatMember(crewMember, awsS3Service);
        message.setSenderNickName(chatMember.getMemberNickName());
        message.setSenderProfileUrl(chatMember.getFileUrl().isEmpty()
                                    ? null
                                    : chatMember.getFileUrl().get(0));

        MyCrewChatMessageEntity entity = MyCrewChatMessageEntity.toEntity(message);
        MyCrewChatMessageEntity saved = chatMessageRepository.save(entity);

        long count = chatMessageRepository.countByCrewId(message.getCrewId());
        if (count > 300) {
            long toDelete = count - 300;
            List<MyCrewChatMessageEntity> oldest = chatMessageRepository.findByCrewId(
                message.getCrewId(),
                PageRequest.of(0, (int) toDelete, Sort.by("createTime").ascending())
            );
            chatMessageRepository.deleteAll(oldest);
        }
        
        return MyCrewChatMessageDto.toDto(saved);
    }

    @Override
    public List<MyCrewChatMessageDto> recentMessages(Long crewId, int limit) {
        List<MyCrewChatMessageEntity> list = chatMessageRepository.findByCrewId(
            crewId,
            PageRequest.of(0, limit, Sort.by("createTime").descending())
        );

        Map<Long, CrewMemberDto> memberMap = crewMemberRepository.findAllByCrewEntityId(crewId)
            .stream()
            .map(mem -> CrewMemberDto.toCrewChatMember(mem, awsS3Service))
            .collect(Collectors.toMap(CrewMemberDto::getMemberId, m -> m));

        List<MyCrewChatMessageDto> dtoList = list.stream()
                .map(msg -> {
                    CrewMemberDto member = memberMap.get(msg.getSenderId());
                    String nickName = member != null ? member.getMemberNickName() : "알 수 없음";
                    String profileUrl = (member != null && member.getFileUrl() != null && !member.getFileUrl().isEmpty()) 
                                        ? member.getFileUrl().get(0) : null;
                                         
                    return MyCrewChatMessageDto.toDto2(msg, nickName, profileUrl);
                })
                .toList();               
        
        return dtoList;
    }

    @Override
    public MyCrewChatMessageDto enterChat(Long crewId, Long memberId) throws IOException {
        CrewMemberEntity crewMember = crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 크루의 멤버가 아닙니다."));
        
        CrewMemberDto member = CrewMemberDto.toCrewChatMember(crewMember, awsS3Service);

        MyCrewChatMessageDto message = MyCrewChatMessageDto.builder()
                .crewId(crewId)
                .senderId(memberId)
                .senderNickName(member.getMemberNickName())
                .senderProfileUrl(member.getFileUrl().isEmpty() ? null : member.getFileUrl().get(0))
                .message(member.getMemberNickName()+"님이 입장했습니다.")
                .createTime(LocalDateTime.now())
                .type(ChatMessageType.ENTER)
                .build();

        return saveMessage(message);

    } 

    @Override
    public MyCrewChatMessageDto leaveChat(Long crewId, Long memberId) throws IOException {
        CrewMemberEntity crewMember = crewMemberRepository.findByCrewEntityIdAndMemberEntityId(crewId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 크루의 멤버가 아닙니다."));
        
        CrewMemberDto member = CrewMemberDto.toCrewChatMember(crewMember, awsS3Service);

        MyCrewChatMessageDto message = MyCrewChatMessageDto.builder()
                .crewId(crewId)
                .senderId(memberId)
                .senderNickName(member.getMemberNickName())
                .senderProfileUrl(member.getFileUrl().isEmpty() ? null : member.getFileUrl().get(0))
                .message(member.getMemberNickName()+"님이 퇴장했습니다.")
                .createTime(LocalDateTime.now())
                .type(ChatMessageType.LEAVE)
                .build();
        
        return saveMessage(message);

    }

    @Override
    public int getActiveCount(Long crewId) {
        
        return 10;
    } 
    
}
