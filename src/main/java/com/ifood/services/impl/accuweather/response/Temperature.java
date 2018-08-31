package com.ifood.services.impl.accuweather.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperature {

    @JsonProperty("Metric")
    private TemperatureInfo metric;

    @JsonProperty("Imperial")
    private TemperatureInfo imperial;

    public TemperatureInfo getMetric() {
        return metric;
    }

    public TemperatureInfo getImperial() {
        return imperial;
    }

}
