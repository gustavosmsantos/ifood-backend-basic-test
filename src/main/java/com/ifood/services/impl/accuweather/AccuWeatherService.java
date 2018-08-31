package com.ifood.services.impl.accuweather;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.Details;
import com.ifood.model.Weather;
import com.ifood.model.WeatherInfo;
import com.ifood.services.WeatherService;
import com.ifood.services.impl.accuweather.response.Temperature;
import com.ifood.services.impl.accuweather.response.TemperatureInfo;
import com.ifood.services.impl.accuweather.response.location.AccuWeatherLocationResponse;
import com.ifood.services.impl.accuweather.response.AccuWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component(value = "fallbackWeatherService")
public class AccuWeatherService implements WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccuWeatherService.class);

    private RestTemplate restTemplate;

    @Value("${accuweather.location.url}")
    private String locationUrl;

    @Value("${accuweather.weather.url}")
    private String weatherUrl;

    @Value("${accuweather.key}")
    private String key;

    public AccuWeatherService(@Autowired RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    @Override
    public WeatherInfo retrieveWeatherForCity(String city, TemperatureUnitsEnum temperatureUnits) throws EntityNotFoundException {
        try {
            ResponseEntity<List<AccuWeatherLocationResponse>> entity = locationRequest(city);
            List<AccuWeatherLocationResponse> cities = entity.getBody();
            if (cities.isEmpty()) {
                throw new EntityNotFoundException("city", city);
            }
            AccuWeatherLocationResponse location = cities.get(0);
            String locationKey = location.getKey();

            ResponseEntity<List<AccuWeatherResponse>> response = weatherRequest(locationKey);
            List<AccuWeatherResponse> weatherList = response.getBody();
            if (weatherList.isEmpty()) {
                throw new RuntimeException();
            }

            return this.convert(location, weatherList.get(0), temperatureUnits);
        } catch (HttpStatusCodeException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if (HttpStatus.NOT_FOUND.equals(statusCode)) {
                throw new EntityNotFoundException("city", city);
            }
            throw ex;
        }
    }

    private WeatherInfo convert(AccuWeatherLocationResponse location, AccuWeatherResponse weatherResponse, TemperatureUnitsEnum units) {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setCity(location.getArea().getCity());
        weatherInfo.setCountry(location.getArea().getCountry());

        Weather weather = new Weather();
        weather.setMain(weatherResponse.getWeatherText());
        weatherInfo.setWeather(weather);

        Details details = new Details();
        TemperatureInfo temperature = getTemperature(weatherResponse, units);
        if (temperature != null) {
            details.setTemperatureUnit(units);
            details.setTemperature(temperature.getValue());
        }

        weatherInfo.setDetails(details);
        return weatherInfo;
    }

    private TemperatureInfo getTemperature(AccuWeatherResponse weatherResponse, TemperatureUnitsEnum unit) {
        Temperature temperature = weatherResponse.getTemperature();
        if (TemperatureUnitsEnum.CELSIUS.equals(unit)) {
            return temperature.getMetric();
        } else if (TemperatureUnitsEnum.FAHRENHEIT.equals(unit)) {
            return temperature.getImperial();
        }

        //TODO: kelvin
        return null;
    }

    private ResponseEntity<List<AccuWeatherLocationResponse>> locationRequest(String city) {
        String uri = UriComponentsBuilder.fromHttpUrl(locationUrl)
                .queryParam("q", city)
                .queryParam("apikey", key).build(false).toUriString();

        LOGGER.info("Calling the endpoint \"{}\", for city \"{}\"", uri, city);
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<AccuWeatherLocationResponse>>() {
        });
    }

    private ResponseEntity<List<AccuWeatherResponse>> weatherRequest(String location) {
        String uri = UriComponentsBuilder.fromHttpUrl(weatherUrl)
                .path(location)
                .queryParam("apikey", key).build(false).toUriString();

        LOGGER.info("Calling the endpoint \"{}\", for city \"{}\"", uri, location);
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<AccuWeatherResponse>>() {
        });
    }

}
