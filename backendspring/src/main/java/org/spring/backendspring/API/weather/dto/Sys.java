package org.spring.backendspring.API.weather.dto;

import lombok.Data;

@Data
public class Sys {
    private String country;
    private String id;
    private String sunrise;
    private String sunset;
    private String type;
}
