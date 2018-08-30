package com.ifood.services.impl.openweather;

import com.ifood.enums.temperature.TemperatureUnitsEnum;

import java.util.HashMap;
import java.util.Map;

public class UnitsRetriever {

    private static Map<TemperatureUnitsEnum, String> units = new HashMap<>();

    static {
        units.put(TemperatureUnitsEnum.FAHRENHEIT, "imperial");
        units.put(TemperatureUnitsEnum.CELSIUS, "metric");
        units.put(TemperatureUnitsEnum.KELVIN, "");
    }

    public static String getUnits(TemperatureUnitsEnum units) {
        return UnitsRetriever.units.get(units);
    }

}
