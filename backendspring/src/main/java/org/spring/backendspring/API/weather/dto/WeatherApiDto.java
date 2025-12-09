package org.spring.backendspring.API.weather.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiDto {

    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private String cod;
    private String dt;
    private String id;
    private String name;
    private Sys sys;
    private String timezone;
    private String visibility;
}
