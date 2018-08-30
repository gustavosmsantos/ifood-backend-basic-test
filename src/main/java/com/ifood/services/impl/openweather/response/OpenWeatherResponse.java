package com.ifood.services.impl.openweather.response;

import com.ifood.model.Weather;

import java.time.Instant;
import java.util.List;

public class OpenWeatherResponse {

    private Instant dt;

    private String name;

    private Sys sys;

    private List<Weather> weather;

    private Main main;

    public Instant getDt() {
        return dt;
    }

    public void setDt(Instant dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "OpenWeatherResponse{" +
                "dt=" + dt +
                ", name='" + name + '\'' +
                ", sys=" + sys +
                ", weather=" + weather +
                ", main=" + main +
                '}';
    }
}