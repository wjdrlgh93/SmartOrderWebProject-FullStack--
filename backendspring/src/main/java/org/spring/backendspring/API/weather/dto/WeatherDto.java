package org.spring.backendspring.API.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

    private Long id;

    private String name;

    private String lat;

    private String lon;

    private String country;

    private String temp_max;

    private String temp_min;
}
