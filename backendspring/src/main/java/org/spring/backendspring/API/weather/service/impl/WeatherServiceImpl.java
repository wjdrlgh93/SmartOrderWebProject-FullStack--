package org.spring.backendspring.API.weather.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.spring.backendspring.API.weather.dto.WeatherApiDto;
import org.spring.backendspring.API.weather.entity.WeatherEntity;
import org.spring.backendspring.API.weather.repository.WeatherRepository;
import org.spring.backendspring.API.weather.service.WeatherService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void insertWeather(String responseBody) {
        try {

            WeatherApiDto weatherApiDto = objectMapper.readValue(responseBody, WeatherApiDto.class);

            log.info("API 응답 파싱 완료: {}", weatherApiDto);


            Optional<WeatherEntity> weatherEntity = weatherRepository.findByName(weatherApiDto.getName());


            if (!weatherEntity.isPresent()) {
                WeatherEntity newEntity = WeatherEntity.builder()
                        .name(weatherApiDto.getName())
                        .lat(weatherApiDto.getCoord().getLat())
                        .lon(weatherApiDto.getCoord().getLon())
                        .icon(weatherApiDto.getWeather().get(0).getIcon())
                        .temp_min(Double.parseDouble(weatherApiDto.getMain().getTemp_min()))
                        .temp_max(Double.parseDouble(weatherApiDto.getMain().getTemp_max()))
                        .country(weatherApiDto.getSys().getCountry())
                        .build();

                weatherRepository.save(newEntity);
                log.info("새로운 날씨 정보 저장 완료: {}", newEntity);
            } else {
                log.info("이미 저장된 날씨 정보 존재 - 업데이트 또는 무시 가능");

            }

        } catch (Exception e) {
            log.error("날씨 정보 처리 중 예외 발생", e);
        }
    }
}