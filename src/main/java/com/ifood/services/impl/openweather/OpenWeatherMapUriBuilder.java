package com.ifood.services.impl.openweather;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static com.ifood.services.impl.openweather.UnitsRetriever.getUnits;

@Component
public class OpenWeatherMapUriBuilder {

    @Value("${openweathermap.url}")
    private String url;

    @Value("${openweathermap.appid}")
    private String appId;

    @Value("${weather.language}")
    private String lang;

    public String build(String city, TemperatureUnitsEnum temperatureUnits) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", city)
                .queryParam("appid", appId)
                .queryParam("lang", lang)
                .queryParam("units", getUnits(temperatureUnits)).build(false).toUriString();
    }

}
