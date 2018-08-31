package com.ifood.services.impl.accuweather.response.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdministrativeArea {

    @JsonProperty("EnglishName")
    private String city;

    @JsonProperty("CountryID")
    private String country;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
