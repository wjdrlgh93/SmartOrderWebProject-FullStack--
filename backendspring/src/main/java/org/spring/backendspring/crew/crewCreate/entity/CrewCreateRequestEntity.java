package org.spring.backendspring.crew.crewCreate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.RequestStatus;
import org.spring.backendspring.member.entity.MemberEntity;

import org.spring.backendspring.crew.crewCreate.dto.CrewCreateRequestDto;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "crew_create_request_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewCreateRequestEntity extends BasicTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crew_create_request_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity; // 신청자


    @Column(nullable = false)
    private String crewName; // 요청한 크루명


    @Column(nullable = false)
    private String message; // 메모

    @Column(nullable = false)
    private String district; // 지역
    
    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private RequestStatus status; // PENDING/APPROVED/REJECTED

    public static CrewCreateRequestEntity toEntity(CrewCreateRequestDto dto, RequestStatus status) {
        
        return CrewCreateRequestEntity.builder()
                .crewName(dto.getCrewName())
                .message(dto.getMessage())
                .district(dto.getDistrict())
                .memberEntity(dto.getMemberEntity())
                .status(status)
                .build();
    }
}