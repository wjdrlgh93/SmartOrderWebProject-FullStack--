package org.spring.backendspring.crew.crewRun.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendspring.common.AttendeeStatus;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.member.entity.MemberEntity;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "crew_run_member_tb")
public class CrewRunMemberEntity extends BasicTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crew_run_member_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_run_id", nullable = false)
    private CrewRunEntity crewRunEntity; // 어떤 일정에 참석?


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity; // 누가 참석?


    // @Enumerated(EnumType.STRING)
    // private AttendeeStatus status; // YES / NO

    public static CrewRunMemberEntity insertCrewRun(CrewRunMemberEntity crewRunMemberEntity){
        return CrewRunMemberEntity.builder()
                .crewRunEntity(crewRunMemberEntity.getCrewRunEntity())
                .memberEntity(crewRunMemberEntity.getMemberEntity())
                .build();
    }
}