package org.spring.backendspring.crew.crewCreate.service.impl;

import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.member.repository.MemberRepository;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crew.repository.CrewRepository;
import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;
import org.spring.backendspring.crew.crewCreate.entity.CrewCreateRequestEntity;
import org.spring.backendspring.crew.crewCreate.repository.CrewCreateRequestRepository;
import org.spring.backendspring.crew.crewCreate.service.CrewCreateRequestService;
import org.spring.backendspring.crew.crewMember.entity.CrewMemberEntity;
import org.spring.backendspring.crew.crewMember.repository.CrewMemberRepository;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewCreateRequestServiceImpl implements CrewCreateRequestService {

    private final CrewRepository crewRepository;
    private final CrewCreateRequestRepository crewCreateRequestRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final MemberRepository memberRepository;
    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    @Override
    public void createRequest(CrewCreateRequestDto dto, Long loginUserId) {
        
        MemberEntity member = memberRepository.findById(loginUserId)
                        .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원"));

        dto.setMemberEntity(member);

        CrewCreateRequestEntity entity = CrewCreateRequestEntity.toEntity(dto, RequestStatus.PENDING);

        crewCreateRequestRepository.save(entity);
    }

    @Override
    public void approveRequest(Long requestId) {
        
        CrewCreateRequestEntity requestEntity = crewCreateRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청"));

        if (requestEntity.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 요청");
        }
        
        requestEntity.setStatus(RequestStatus.APPROVED);

        crewCreateRequestRepository.save(requestEntity);

        CrewEntity crewEntity = CrewEntity.builder()
                        .name(requestEntity.getCrewName())
                        .memberEntity(requestEntity.getMemberEntity())
                        .description(requestEntity.getMessage())
                        .district(requestEntity.getDistrict())
                        .build();
        System.out.println(crewEntity.getMemberEntity());
        crewRepository.save(crewEntity);

        CrewMemberEntity leader = CrewMemberEntity.builder()
                        .crewEntity(crewEntity)
                        .memberEntity(requestEntity.getMemberEntity())
                        .roleInCrew(CrewRole.LEADER)
                        .build();

        crewMemberRepository.save(leader);

        // RabbitMQ 큐 생성 - 바인딩
        // String queueName = "chat.queue.crew" + crewEntity.getId();
        // String routingKey = "chat.key.crew" + crewEntity.getId();
        // Queue queue = new Queue(queueName, true);
        // rabbitAdmin.declareQueue(queue);
        // rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));

        // 알림 if wanted
        // String message = crewEntity.getName() + "크루 신청이 승인되었습니다.";
        // rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, message);

    }

    @Override
    public void rejectRequest(Long requestId) {
        CrewCreateRequestEntity entity = crewCreateRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청"));
        
        if (entity.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 요청");
        }

        entity.setStatus(RequestStatus.REJECTED);
        crewCreateRequestRepository.save(entity);

    }

    
}