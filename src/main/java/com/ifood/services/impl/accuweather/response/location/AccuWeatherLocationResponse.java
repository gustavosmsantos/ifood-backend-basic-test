package com.ifood.services.impl.accuweather.response.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccuWeatherLocationResponse {

    @JsonProperty("Key")
    private String key;

    @JsonProperty("AdministrativeArea")
    private AdministrativeArea area;

    public String getKey() {
        return key;
    }

    public AdministrativeArea getArea() {
        return area;
    }

}
