package com.ifood.services.impl.openweather;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.Details;
import com.ifood.model.WeatherInfo;
import com.ifood.services.WeatherService;
import com.ifood.services.impl.openweather.response.Main;
import com.ifood.services.impl.openweather.response.OpenWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class OpenWeatherMapService implements WeatherService {

    private Logger LOGGER = LoggerFactory.getLogger(OpenWeatherMapService.class);

    private RestTemplate restTemplate;

    private OpenWeatherMapUriBuilder uriBuilder;

    public OpenWeatherMapService(@Autowired RestTemplateBuilder restTemplate, @Autowired OpenWeatherMapUriBuilder uriBuilder) {
        this.restTemplate = restTemplate.build();
        this.uriBuilder = uriBuilder;
    }

    public WeatherInfo retrieveWeatherForCity(String city, TemperatureUnitsEnum temperatureUnits) throws EntityNotFoundException {

        String uri = uriBuilder.build(city, temperatureUnits);
        LOGGER.info("Calling the endpoint \"{}\", for city \"{}\"", uri, city);

        try {
            ResponseEntity<OpenWeatherResponse> entity = restTemplate.getForEntity(uri, OpenWeatherResponse.class);
            return this.convert(entity.getBody(), temperatureUnits);
        } catch (HttpStatusCodeException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if (HttpStatus.NOT_FOUND.equals(statusCode)) {
                throw new EntityNotFoundException("city", city);
            }
            throw ex;
        }

    }

    private WeatherInfo convert(OpenWeatherResponse response, TemperatureUnitsEnum temperatureUnit) {
        LOGGER.info("Converting response: {}", response);

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setTime(LocalDateTime.now());
        weatherInfo.setCity(response.getName());
        weatherInfo.setCountry(response.getSys().getCountry());

        weatherInfo.setWeather(response.getWeather().get(0));

        Main mainResponse = response.getMain();
        Details details = new Details();
        details.setTemperatureUnit(temperatureUnit);
        details.setTemperature(mainResponse.getTemp());
        details.setMinTemperature(mainResponse.getTempMin());
        details.setMaxTemperature(mainResponse.getTempMax());
        details.setPressure(mainResponse.getPressure());
        details.setHumidity(mainResponse.getHumidity());
        weatherInfo.setDetails(details);

        return weatherInfo;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
