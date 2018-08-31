package com.ifood.services;

import com.ifood.enums.temperature.TemperatureUnitsEnum;
import com.ifood.exception.EntityNotFoundException;
import com.ifood.services.impl.openweather.OpenWeatherMapUriBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherMapUriBuilderTest {

    @Value("${openweathermap.url}")
    private String baseUrl;

    @Autowired
    private OpenWeatherMapUriBuilder uriBuilder;

    @Test
    public void testParameterFahrenheitUnit() throws EntityNotFoundException {
        String uri = uriBuilder.build("São Paulo", TemperatureUnitsEnum.FAHRENHEIT);
        assertThat(uri, is(getUrl("imperial")));
    }

    @Test
    public void testParameterKelvinUnit() throws EntityNotFoundException {
        String uri = uriBuilder.build("São Paulo", TemperatureUnitsEnum.KELVIN);
        assertThat(uri, is(getUrl("")));
    }

    @Test
    public void testParameterCelsiusUnit() throws EntityNotFoundException {
        String uri = uriBuilder.build("São Paulo", TemperatureUnitsEnum.CELSIUS);
        assertThat(uri, is(getUrl("metric")));
    }

    private String getUrl(String units) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("q", "São Paulo")
                .queryParam("appid", "8199304b927600d08c479986ff37d5b4")
                .queryParam("lang", "en")
                .queryParam("units", units).build(false).toUriString();
    }

}
