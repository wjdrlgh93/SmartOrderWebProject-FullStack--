package org.spring.backendspring.API.weather.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "weather29")
public class WeatherEntity {

    @Id
    @Column(name = "weather_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double lat;
    private double lon;
    private String country;
    private double temp_max;
    private double temp_min;
    private String icon;
}
