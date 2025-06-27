package com.weather.weather_prediction.model;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherForecast {
    private LocalTime time;
    private double windSpeed;
    private double highTemp;
    private double lowTemp;
    private int humidity;
    private List<Weather> weatherList;

}
