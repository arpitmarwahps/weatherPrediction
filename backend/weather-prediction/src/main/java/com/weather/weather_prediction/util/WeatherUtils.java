package com.weather.weather_prediction.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weather_prediction.dto.WeatherApiResponse;

public class WeatherUtils {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static WeatherApiResponse parseForecast(JsonNode root) throws JsonProcessingException {
        WeatherApiResponse weatherApiResponse = objectMapper.treeToValue(root, WeatherApiResponse.class);
        return weatherApiResponse;
    }
}
