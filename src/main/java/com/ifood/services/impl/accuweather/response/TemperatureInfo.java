package com.ifood.services.impl.accuweather.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemperatureInfo {

    @JsonProperty("Value")
    private double value;

    @JsonProperty("Unit")
    private String unit;

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

}
