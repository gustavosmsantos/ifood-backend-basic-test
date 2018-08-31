package com.ifood.controllers;

import com.ifood.DemoApplication;
import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.model.WeatherInfo;
import com.ifood.services.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WeatherControllerCacheTest.Config.class)
public class WeatherControllerCacheTest {

    @Autowired
    private WeatherController controller;

    @Autowired
    @Qualifier("openWeatherMapService")
    private WeatherService weatherService;

    @Test
    public void weatherShouldBeUsingCache() throws Exception {
        WeatherInfo first = new WeatherInfo();
        WeatherInfo second = new WeatherInfo();
        WeatherInfo third = new WeatherInfo();
        when(weatherService.retrieveWeatherForCity(anyString(), any())).thenReturn(first, second, third);

        WeatherInfo firstCall = controller.retrieveWeather("firstCity", TemperatureUnitsEnum.FAHRENHEIT);
        WeatherInfo secondCall = controller.retrieveWeather("firstCity", TemperatureUnitsEnum.FAHRENHEIT);
        WeatherInfo thirdCall = controller.retrieveWeather("anotherCity", TemperatureUnitsEnum.FAHRENHEIT);
        WeatherInfo fourthCall = controller.retrieveWeather("firstCity", TemperatureUnitsEnum.CELSIUS);

        assertEquals(first, firstCall);
        assertEquals(first, secondCall);
        assertEquals(second, thirdCall);
        assertEquals(third, fourthCall);
    }

    @PropertySource(value = "application.properties")
    @Import(DemoApplication.class)
    @Configuration
    static class Config {

        @Bean(name = "openWeatherMapService")
        public WeatherService weatherService() {
            return Mockito.mock(WeatherService.class);
        }

    }

}

