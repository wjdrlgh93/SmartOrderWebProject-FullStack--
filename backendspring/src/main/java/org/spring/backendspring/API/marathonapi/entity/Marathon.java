package org.spring.backendspring.API.marathonapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // 대회명
    private String date;      // 대회일시
    private String location;  // 대회장소
    private String category;  // 종목
    private String host;      // 주최
}
