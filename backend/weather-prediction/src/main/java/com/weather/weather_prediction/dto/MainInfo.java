package com.weather.weather_prediction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainInfo {

    @JsonProperty("temp")
    Double temp;

    @JsonProperty("feels_like")
    Double feelsLike;

    @JsonProperty("temp_min")
    Double tempMin;

    @JsonProperty("temp_max")
    Double tempMax;

    @JsonProperty("pressure")
    Integer pressure;

    @JsonProperty("sea_level")
    Integer seaLevel;

    @JsonProperty("grnd_level")
    Integer groundLevel;

    @JsonProperty("humidity")
    Integer humidity;

    @JsonProperty("temp_kf")
    Double tempKf;
}
