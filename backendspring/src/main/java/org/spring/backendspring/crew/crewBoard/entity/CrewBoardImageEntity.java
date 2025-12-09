package org.spring.backendspring.crew.crewBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendspring.common.BasicTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "crew_board_image_tb")
public class CrewBoardImageEntity extends BasicTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crew_board_image_id")
    private Long id;


    @Column(nullable = false)
    private String newName;

    private String oldName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_board_id")
    private CrewBoardEntity crewBoardEntity;

    public static CrewBoardImageEntity toCrewBoardImageEntity(CrewBoardEntity crewBoardEntity1, String originalFileName, String newFileName) {
        return CrewBoardImageEntity.builder()
                .crewBoardEntity(crewBoardEntity1)
                .newName(newFileName)
                .oldName(originalFileName)
                .build();
    }
}