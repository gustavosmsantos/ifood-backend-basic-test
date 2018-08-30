package com.ifood.model;

import com.ifood.enums.temperature.TemperatureUnitsEnum;

public class Details {

    private TemperatureUnitsEnum temperatureUnit;

    private double temperature;

    private double minTemperature;

    private double maxTemperature;

    private Integer pressure;

    private Integer humidity;

    public TemperatureUnitsEnum getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(TemperatureUnitsEnum temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

}
