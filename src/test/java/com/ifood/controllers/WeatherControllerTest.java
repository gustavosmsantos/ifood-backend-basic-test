package com.ifood.controllers;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.services.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService service;

    @InjectMocks
    private WeatherController controller;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void callWithNoUnitAssumeFahrenheit() throws Exception {
        mockMvc.perform(get("/weather").param("city", "somecity"))
                .andExpect(status().is(200));
        verify(service).retrieveWeatherForCity("somecity", TemperatureUnitsEnum.FAHRENHEIT);
    }

    @Test
    public void callUsingCelsiusUnit() throws Exception {
        mockMvc.perform(get("/weather").param("city", "somecity")
            .param("units", "CELSIUS"))
                .andExpect(status().is(200));
        verify(service).retrieveWeatherForCity("somecity", TemperatureUnitsEnum.CELSIUS);
    }

    @Test
    public void callUsingKelvinUnit() throws Exception {
        mockMvc.perform(get("/weather").param("city", "somecity")
                .param("units", "KELVIN"))
                .andExpect(status().is(200));
        verify(service).retrieveWeatherForCity("somecity", TemperatureUnitsEnum.KELVIN);
    }

}

