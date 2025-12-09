package org.spring.backendspring.crew.crew.entity;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendspring.common.BasicTime;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "crew_image_tb")
public class CrewImageEntity extends BasicTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crew_image_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private CrewEntity crewEntity; // 어떤 크루의 이미지인가

    private String newName;

    private String oldName;

    public static CrewImageEntity toEntity(CrewEntity crewEntity1, String originalImageName, String newImageName) {

        return CrewImageEntity.builder()
                .crewEntity(crewEntity1)
                .newName(newImageName)
                .oldName(originalImageName)
                .build();
    }
}