package org.spring.backendspring.API.weather.controller;

import lombok.RequiredArgsConstructor;

import org.spring.backendspring.API.weather.service.WeatherService;
import org.spring.backendspring.config.security.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;
    @Value("${open.openWeatherMap.serviceKey}")
    private String key;

    @GetMapping("/search/{q}")
    public ResponseEntity<?> search(@PathVariable("q") String q) {
        //URL
        String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + q + "&appid=" + key;
        //Header
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-type", "application/json");
        String responseBody= OpenApiUtil.get(apiURL,requestHeaders);
        System.out.println(responseBody+" responseBody");
        // JSON -> JAVA 변경 -> DTO에추가 -> Entity -> DB저장
        weatherService.insertWeather(responseBody);

        Map<String,String> weather=new HashMap<>();
        weather.put("weather",responseBody);
        return ResponseEntity.status(HttpStatus.OK).body(weather);
    }
}
