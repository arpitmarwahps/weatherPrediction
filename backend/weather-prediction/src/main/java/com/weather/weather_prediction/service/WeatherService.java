package com.weather.weather_prediction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.weather.weather_prediction.client.OpenWeatherClient;
import com.weather.weather_prediction.dto.WeatherApiResponse;
import com.weather.weather_prediction.dto.WeatherInfoDto;
import com.weather.weather_prediction.dto.Wind;
import com.weather.weather_prediction.model.WeatherResponse;
import com.weather.weather_prediction.util.WeatherUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Autowired
    private OpenWeatherClient openWeatherClient;

    public List<WeatherResponse> getWeatherForecast(String city) throws JsonProcessingException {
        JsonNode forecastJson = openWeatherClient.getForecast(city);
        WeatherApiResponse weatherApiResponse = WeatherUtils.parseForecast(forecastJson);
        return formWeatherForecast(weatherApiResponse);
    }

    private List<WeatherResponse> formWeatherForecast(WeatherApiResponse weatherApiResponse) {

        Map<LocalDate, List<WeatherInfoDto>> groupedByDate = new HashMap<>();

        // 1. Group entries by date
        for (WeatherInfoDto weatherInfo: weatherApiResponse.getWeatherInfoDtoList()) {
            String dtTxt = weatherInfo.getDate(); // e.g., "2025-06-23 12:00:00"
            LocalDate date = LocalDate.parse(dtTxt.substring(0, 10)); // "2025-06-23"
            groupedByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(weatherInfo);
        }

        List<WeatherResponse> weatherResponseList = new ArrayList<>();

        for (Map.Entry<LocalDate, List<WeatherInfoDto>> entry : groupedByDate.entrySet()) {
            WeatherResponse weatherResponse = new WeatherResponse();
            Set<String> adviceSet = new HashSet<>();
            LocalDate date = entry.getKey();
            weatherResponse.setDate(date);
            List<WeatherInfoDto> weatherInfoDtoList = entry.getValue();
            weatherResponse.setWeatherInfoDtoList(weatherInfoDtoList);

            double minTemp = Double.MAX_VALUE;
            double maxTemp = Double.MIN_VALUE;
            double maxWind = 0;
            double humidity = 0;
            int size = 0;

            for(WeatherInfoDto weatherInfoDto: weatherInfoDtoList) {
                size = weatherInfoDtoList.size();
                minTemp = Math.min(minTemp, weatherInfoDto.getMain().getTempMin());
                maxTemp = Math.max(maxTemp, weatherInfoDto.getMain().getTempMax());
                humidity = humidity + weatherInfoDto.getMain().getHumidity();
                weatherInfoDto.getWeatherList().stream()
                        .forEach(weatherDto -> {
                            String main = weatherDto.getMain().toLowerCase();
                            if (main.contains("rain")) {
                                adviceSet.add("rain");
                            }
                            if (main.contains("thunderstorm")) {
                                adviceSet.add("thunderstorm");
                            }
                            if (main.contains("clouds")) {
                                adviceSet.add("clouds");
                            }
                        });
                Wind wind = weatherInfoDto.getWind();
                double windSpeed = wind.getSpeed() * 2.23694;

                maxWind = Math.max(maxWind, windSpeed);

                if(maxTemp > 40) {
                    adviceSet.add("high temperature");
                }
                if(maxWind > 10) {
                    adviceSet.add("windy");
                }
            }
            humidity = humidity / size;
            weatherResponse.setHumidity(humidity);
            weatherResponse.setLowTemp(minTemp);
            weatherResponse.setHighTemp(maxTemp);
            if(adviceSet.isEmpty()) {
                adviceSet.add("clear sky");
            }
            weatherResponse.setAdvice(StringUtils.join(adviceSet, "|"));
            weatherResponseList.add(weatherResponse);
        }
        return weatherResponseList.stream()
                .sorted(Comparator.comparing(WeatherResponse::getDate))
                .limit(3)
                .collect(Collectors.toList());
    }
}
