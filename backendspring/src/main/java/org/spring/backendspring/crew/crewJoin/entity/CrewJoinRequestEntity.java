package org.spring.backendspring.crew.crewJoin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewJoin.dto.CrewJoinRequestDto;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "crew_join_request_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewJoinRequestEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_join_request_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private CrewEntity crewEntity; // 어떤 크루에 신청?

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity; // 신청자

    @Column(nullable = false)
    private String message; // 메모

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status; // PENDING/APPROVED/REJECTED

    public static CrewJoinRequestEntity insertCrewJoinRequest(CrewJoinRequestDto crewJoinRequestDto){

        return CrewJoinRequestEntity.builder()
                .crewEntity(crewJoinRequestDto.getCrewEntity())
                .memberEntity(crewJoinRequestDto.getMemberEntity())
                .message(crewJoinRequestDto.getMessage())
                .status(RequestStatus.PENDING)
                .build();
    }

    public static CrewJoinRequestEntity updateCrewJoinApproved(CrewJoinRequestEntity crewJoinRequestEntity){

        return CrewJoinRequestEntity.builder()
                .id(crewJoinRequestEntity.getId())
                .crewEntity(crewJoinRequestEntity.getCrewEntity())
                .memberEntity(crewJoinRequestEntity.getMemberEntity())
                .message(crewJoinRequestEntity.getMessage())
                .status(RequestStatus.APPROVED)
                .build();
    }

    public static CrewJoinRequestEntity updateCrewJoinRejected(CrewJoinRequestEntity crewJoinRequestEntity){

        return CrewJoinRequestEntity.builder()
                .id(crewJoinRequestEntity.getId())
                .crewEntity(crewJoinRequestEntity.getCrewEntity())
                .memberEntity(crewJoinRequestEntity.getMemberEntity())
                .message(crewJoinRequestEntity.getMessage())
                .status(RequestStatus.REJECTED)
                .build();
    }

}