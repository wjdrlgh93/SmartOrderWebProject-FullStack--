package org.spring.backendspring.API.weather.repository;

import org.spring.backendspring.API.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {


    Optional<WeatherEntity>  findAllByName(String name);

    Optional<WeatherEntity> findByName(String name);

    Optional<WeatherEntity> findByNameAndLatAndLonAndCountry(String name, Double lat, Double lon, String country);
}
