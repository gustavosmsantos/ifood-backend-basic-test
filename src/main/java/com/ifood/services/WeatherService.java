package com.ifood.services;

import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.WeatherInfo;
import com.ifood.enums.temperature.TemperatureUnitsEnum;

public interface WeatherService {

    WeatherInfo retrieveWeatherForCity(String city, TemperatureUnitsEnum temperatureUnits) throws EntityNotFoundException;

}
