package com.ifood.services;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.exception.EntityNotFoundException;
import com.ifood.model.Details;
import com.ifood.model.Weather;
import com.ifood.model.WeatherInfo;
import com.ifood.services.impl.openweather.OpenWeatherMapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(OpenWeatherMapService.class)
public class OpenWeatherMapServiceTest {

    @Autowired
    private OpenWeatherMapService service;

    @Autowired
    private MockRestServiceServer server;

    @Value("classpath:weather-response-example.json")
    private Resource responseExample;

    private static final String url = "http://api.openweathermap.org/data/2.5/weather?q=S%C3%A3o%20Paulo&appid=8199304b927600d08c479986ff37d5b4&lang=en&units=metric";

    @Test
    public void testSuccessfullResponseAndParse() throws EntityNotFoundException, IOException {
        String example = new String(Files.readAllBytes(responseExample.getFile().toPath()));
        server.expect(requestTo(url))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON_UTF8).body(example));

        WeatherInfo weatherInfo = service.retrieveWeatherForCity("São Paulo", TemperatureUnitsEnum.CELSIUS);

        assertNotNull(weatherInfo);
        assertTrue(weatherInfo.getTime().isBefore(LocalDateTime.now()));
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
        server.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON_UTF8));
        service.retrieveWeatherForCity("São Paulo", TemperatureUnitsEnum.CELSIUS);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testParameterFahrenheitUnit() throws EntityNotFoundException {
        server.expect(requestTo(getUrl("imperial"))).andRespond(withStatus(HttpStatus.NOT_FOUND));
        service.retrieveWeatherForCity("São Paulo", TemperatureUnitsEnum.FAHRENHEIT);
        server.verify();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testParameterKelvinUnit() throws EntityNotFoundException {
        server.expect(requestTo(getUrl(""))).andRespond(withStatus(HttpStatus.NOT_FOUND));
        service.retrieveWeatherForCity("São Paulo", TemperatureUnitsEnum.KELVIN);
        server.verify();
    }

    private String getUrl(String units) {
        String url = "http://api.openweathermap.org/data/2.5/weather";
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", "São Paulo")
                .queryParam("appid", "8199304b927600d08c479986ff37d5b4")
                .queryParam("lang", "en")
                .queryParam("units", units).toUriString();
    }

}
