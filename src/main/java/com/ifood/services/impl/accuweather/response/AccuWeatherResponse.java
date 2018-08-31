package com.ifood.services.impl.accuweather.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccuWeatherResponse {

    @JsonProperty("WeatherText")
    private String weatherText;

    @JsonProperty("Temperature")
    private Temperature temperature;

    public String getWeatherText() {
        return weatherText;
    }

    public Temperature getTemperature() {
        return temperature;
    }

}
