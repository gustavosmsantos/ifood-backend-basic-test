package com.ifood.services.impl.openweather.response;

public class Sys {

    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Sys{" +
                "country='" + country + '\'' +
                '}';
    }

}