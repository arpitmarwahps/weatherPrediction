package com.weather.weather_prediction.model;

import com.weather.weather_prediction.dto.WeatherInfoDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherResponse implements Serializable {
    private LocalDate date;
    private double highTemp;
    private double lowTemp;
    private double humidity;
    private String advice;
    private List<WeatherInfoDto> weatherInfoDtoList;
}
