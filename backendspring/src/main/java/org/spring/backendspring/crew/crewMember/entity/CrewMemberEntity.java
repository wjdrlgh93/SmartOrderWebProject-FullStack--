package org.spring.backendspring.crew.crewMember.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.common.role.CrewRole;
import org.spring.backendspring.crew.crew.entity.CrewEntity;
import org.spring.backendspring.crew.crewJoin.entity.CrewJoinRequestEntity;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "crew_member_tb")
@SQLRestriction("member_id IN (SELECT m.member_id FROM member_tb m WHERE m.is_deleted = FALSE)")
public class CrewMemberEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_member_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private CrewEntity crewEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Enumerated(EnumType.STRING)
    private CrewRole roleInCrew; // LEADER/MEMBER

    @Transient
    public Long getMemberId() {
        return memberEntity != null ? memberEntity.getId() : null;
    }

    public static CrewMemberEntity insertCrewMember (CrewJoinRequestEntity crewJoinRequestEntity){

        return CrewMemberEntity.builder()
                .crewEntity(crewJoinRequestEntity.getCrewEntity())
                .memberEntity(crewJoinRequestEntity.getMemberEntity())
                .roleInCrew(CrewRole.MEMBER)
                .build();
    }


}