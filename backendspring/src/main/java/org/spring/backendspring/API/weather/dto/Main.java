package org.spring.backendspring.API.weather.dto;

import lombok.Data;

@Data
public class Main {
    private String sea_level;
    private String grnd_level;
    private String feels_like;
    private String humidity;
    private String pressure;
    private String temp;
    private String temp_max; //최고 온도
    private String temp_min; //최저 온도
}
