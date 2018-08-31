package com.ifood.controllers;

import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.WeatherInfo;
import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class WeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    private WeatherService weatherService;

    public WeatherController(@Autowired @Qualifier("openWeatherMapService") WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Cacheable(value = "weather", key = "#city + #units")
    @GetMapping("/weather")
    public WeatherInfo retrieveWeather(@RequestParam("city") String city, @RequestParam(required = false) TemperatureUnitsEnum units) throws EntityNotFoundException {
        return weatherService.retrieveWeatherForCity(city, Optional.ofNullable(units).orElse(TemperatureUnitsEnum.FAHRENHEIT));
    }

    @CacheEvict(value = "weather")
    @Scheduled(fixedRate = 15 * 60 * 1000, initialDelay = 1000)
    public void evictAllWeatherCache() {
        LOGGER.info("Cache evicted at {}", LocalDateTime.now());
    }

}
