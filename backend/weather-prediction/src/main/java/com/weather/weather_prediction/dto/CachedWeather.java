package com.weather.weather_prediction.dto;

import com.weather.weather_prediction.model.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachedWeather implements Serializable {
    private LocalDateTime fetchedAt;
    private List<WeatherResponse> data;
}
