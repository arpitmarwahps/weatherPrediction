package com.weather.weather_prediction.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.weather_prediction.client.OpenWeatherClient;
import com.weather.weather_prediction.model.WeatherForecast;
import com.weather.weather_prediction.model.WeatherResponse;
import com.weather.weather_prediction.util.WeatherUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Autowired
    private OpenWeatherClient openWeatherClient;

    public List<WeatherResponse> getWeatherForecast(String city) {
        JsonNode forecastJson = openWeatherClient.getForecast(city);
        List<WeatherResponse> weatherResponses = WeatherUtils.parseForecast(forecastJson);
        return weatherResponses;
    }
}
