package com.ifood.controllers;

import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.WeatherInfo;
import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WeatherController {

    @Autowired
    @Qualifier("openWeatherMapService")
    private WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherInfo retrieveWeather(@RequestParam("city") String city, @RequestParam(required = false) TemperatureUnitsEnum units) throws EntityNotFoundException {
        return weatherService.retrieveWeatherForCity(city, Optional.ofNullable(units).orElse(TemperatureUnitsEnum.FAHRENHEIT));
    }

}
