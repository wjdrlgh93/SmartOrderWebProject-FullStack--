package org.spring.backendspring.crew.crew.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.crew.crew.entity.CrewEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewImageDto {

    private Long id;

    private CrewEntity crewEntity; // 어떤 크루의 이미지인가

    private String newName;

    private String oldName;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
