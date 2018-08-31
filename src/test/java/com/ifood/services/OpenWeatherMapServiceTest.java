package com.ifood.services;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.Details;
import com.ifood.model.Weather;
import com.ifood.model.WeatherInfo;
import com.ifood.services.impl.openweather.OpenWeatherMapService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherMapServiceTest {

    @Autowired
    private OpenWeatherMapService service;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private MockRestServiceServer server;

    @Value("classpath:weather-response-example.json")
    private Resource responseExample;

    @Before
    public void setup() {
        this.server = MockRestServiceServer.bindTo(service.getRestTemplate()).build();
    }

    @Value("${openweathermap.url}")
    private String baseUrl;

    @Test
    public void testSuccessfullResponseAndParse() throws EntityNotFoundException, IOException {
        String example = new String(Files.readAllBytes(responseExample.getFile().toPath()));

        String url = baseUrl + "?q=S%C3%A3o%20Paulo&appid=8199304b927600d08c479986ff37d5b4&lang=en&units=metric";
        server.expect(requestTo(url))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON_UTF8).body(example));

        WeatherInfo weatherInfo = service.retrieveWeatherForCity("São Paulo", TemperatureUnitsEnum.CELSIUS);
        LocalDateTime now = LocalDateTime.now();

        assertNotNull(weatherInfo);
        LocalDateTime weatherTime = weatherInfo.getTime();
        assertTrue(weatherTime.isEqual(now) || weatherTime.isBefore(now));
        assertThat(weatherInfo.getCity(), is("São Paulo"));
        assertThat(weatherInfo.getCountry(), is("BR"));

        Weather weather = weatherInfo.getWeather();
        assertNotNull(weather);
        assertThat(weather.getMain(), is("Clouds"));
        assertThat(weather.getDescription(), is("few clouds"));

        Details details = weatherInfo.getDetails();
        assertNotNull(details);

        assertThat(details.getTemperatureUnit(), is(TemperatureUnitsEnum.CELSIUS));
        assertThat(details.getTemperature(), is(287.63));
        assertThat(details.getMinTemperature(), is(287.15));
        assertThat(details.getMaxTemperature(), is(288.15));
        assertThat(details.getPressure(), is(1026));
        assertThat(details.getHumidity(), is(87));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testResponseCityNotFound() throws EntityNotFoundException {
        String url = baseUrl + "?q=S%C3%A3o%20Paulo&appid=8199304b927600d08c479986ff37d5b4&lang=en&units=metric";
        server.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON_UTF8));
        service.retrieveWeatherForCity("São Paulo", TemperatureUnitsEnum.CELSIUS);
    }

}
