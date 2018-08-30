package com.ifood.services.impl.openweather;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.model.Details;
import com.ifood.model.WeatherInfo;
import com.ifood.services.WeatherService;
import com.ifood.services.impl.openweather.response.Main;
import com.ifood.services.impl.openweather.response.OpenWeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.ifood.services.impl.openweather.UnitsRetriever.getUnits;

@Component
public class OpenWeatherMapService implements WeatherService {

    private Logger LOGGER = LoggerFactory.getLogger(OpenWeatherMapService.class);

    @Value("${openweathermap.url}")
    private String url;

    @Value("${openweathermap.appid}")
    private String appId;

    @Value("${weather.language}")
    private String lang;

    @Autowired
    private RestTemplate restTemplate;

    public WeatherInfo retrieveWeatherForCity(String city, TemperatureUnitsEnum temperatureUnits) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", city)
                .queryParam("appid", appId)
                .queryParam("lang", lang)
                .queryParam("units", getUnits(temperatureUnits));

        LOGGER.info("Calling the endpoint \"{}\", for city \"{}\"", url, city);

        try {
            ResponseEntity<OpenWeatherResponse> entity = restTemplate.getForEntity(uriBuilder.build(false).toUriString(), OpenWeatherResponse.class);
            if (entity.getStatusCode().is2xxSuccessful()) {
                return this.convert(entity.getBody(), temperatureUnits);
            } else {
                //TODO: exception
                throw new RuntimeException();
            }
        } catch (RestClientException e) {
            //TODO: exception
            throw new RuntimeException();
        }

    }

    private WeatherInfo convert(OpenWeatherResponse response, TemperatureUnitsEnum temperatureUnit) {
        LOGGER.info("Converting response: {}", response);

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setTime(LocalDateTime.ofInstant(response.getDt(), ZoneOffset.UTC));
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

}