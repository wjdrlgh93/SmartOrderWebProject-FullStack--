package org.spring.backendspring.API.weather.dto;

import lombok.Data;

@Data
public class WeatherItem {
    private String description;
    private String icon;
    private String id;
    private String main;
}
