package com.weather.weather_prediction.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherResponse {
    private LocalDate date;
    private double highTemp;
    private double lowTemp;
    private String advice;
    private List<WeatherForecast> weatherForecastList;
}
