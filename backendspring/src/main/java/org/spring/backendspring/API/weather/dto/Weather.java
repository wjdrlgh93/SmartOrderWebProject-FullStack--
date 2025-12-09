package org.spring.backendspring.API.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Weather {
    private String icon;
    private String main;
    private String description;
    private int id;

}
