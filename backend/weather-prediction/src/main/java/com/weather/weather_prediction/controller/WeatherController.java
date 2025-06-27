package com.weather.weather_prediction.controller;

import com.weather.weather_prediction.model.WeatherResponse;
import com.weather.weather_prediction.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<List<WeatherResponse>> getWeather(@RequestParam String city) {
        List<WeatherResponse> response = weatherService.getWeatherForecast(city);
        return ResponseEntity.ok(response);
    }
}
