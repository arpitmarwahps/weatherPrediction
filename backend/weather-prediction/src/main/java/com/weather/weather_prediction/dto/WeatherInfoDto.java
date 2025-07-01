package com.weather.weather_prediction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherInfoDto {

    @JsonProperty("main")
    MainInfo main;

    @JsonProperty("weather")
    List<WeatherDto> weatherList;

    @JsonProperty("wind")
    Wind wind;

    @JsonProperty("dt_txt")
    String date;
}
