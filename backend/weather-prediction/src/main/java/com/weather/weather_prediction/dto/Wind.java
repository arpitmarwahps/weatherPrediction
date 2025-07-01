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
public class Wind {

    @JsonProperty("speed")
    Double speed;

    @JsonProperty("deg")
    Integer deg;

    @JsonProperty("gust")
    Double gust;
}
