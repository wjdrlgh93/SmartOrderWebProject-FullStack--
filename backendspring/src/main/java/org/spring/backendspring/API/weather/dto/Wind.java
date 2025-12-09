package org.spring.backendspring.API.weather.dto;

import lombok.Data;

@Data
public class Wind {
    private String deg;
    private String speed;
    private String gust;
}
