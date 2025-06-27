package com.weather.weather_prediction.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.weather_prediction.model.Weather;
import com.weather.weather_prediction.model.WeatherForecast;
import com.weather.weather_prediction.model.WeatherResponse;
import org.apache.catalina.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherUtils {

    public static List<WeatherResponse> parseForecast(JsonNode root) {
        Map<LocalDate, List<JsonNode>> groupedByDate = new HashMap<>();
        List<WeatherResponse> weatherResponseList = new ArrayList<>();

        JsonNode listNode = root.get("list");
        if (listNode == null || !listNode.isArray()) return List.of();

        // 1. Group entries by date
        for (JsonNode entry : listNode) {
            String dtTxt = entry.get("dt_txt").asText(); // e.g., "2025-06-23 12:00:00"
            LocalDate date = LocalDate.parse(dtTxt.substring(0, 10)); // "2025-06-23"
            groupedByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(entry);
        }
        List<WeatherForecast> result = new ArrayList<>();
        // 2. For each date, aggregate values
        for (Map.Entry<LocalDate, List<JsonNode>> entry : groupedByDate.entrySet()) {
            WeatherResponse weatherResponse = new WeatherResponse();
            List<String> adviceList = new ArrayList<>();
            LocalDate date = entry.getKey();
            weatherResponse.setDate(date);
            List<JsonNode> nodes = entry.getValue();
            double minTemp = Double.MAX_VALUE;
            double maxTemp = Double.MIN_VALUE;
            double maxWind = 0;
            boolean rain = false;
            boolean thunder = false;
            List<WeatherForecast> weatherForecastList = new ArrayList<>();
            for (JsonNode node : nodes) {
                LocalTime time = LocalTime.parse(node.get("dt_txt").asText().substring(11));
                WeatherForecast weatherForecast = new WeatherForecast();
                List<Weather> weatherList = new ArrayList<>();
                weatherForecast.setTime(time);
                JsonNode main = node.get("main");
                JsonNode weatherArray = node.get("weather");
                JsonNode wind = node.get("wind");

                if (main != null) {
                    double temp_min = main.get("temp_min").asDouble();
                    double temp_max = main.get("temp_max").asDouble();
                    int humidity = main.get("humidity").asInt();
                    weatherForecast.setLowTemp(temp_min);
                    weatherForecast.setHighTemp(temp_max);
                    weatherForecast.setHumidity(humidity);
                    minTemp = Math.min(minTemp, temp_min);
                    maxTemp = Math.max(maxTemp, temp_max);
                }

                if (weatherArray != null && weatherArray.isArray()) {
                    for (JsonNode w : weatherArray) {
                        Weather weather = new Weather();
                        String condition = w.get("main").asText().toLowerCase();
                        weather.setMain(condition);
                        weather.setId(w.get("id").asInt());
                        weather.setDescription(w.get("description").asText().toLowerCase());
                        weather.setIcon(w.get("icon").asText());
                        weatherList.add(weather);
                        if (condition.contains("rain")) {
                            adviceList.add("Chance of Rain! Carry umbrella");
                        }
                        if (condition.contains("thunderstorm")) {
                            adviceList.add("Don’t step out! A thunderstorm is brewing!");
                        }
                    }
                }
                weatherForecast.setWeatherList(weatherList);

                if (wind != null && wind.has("speed")) {
                    double speed = wind.get("speed").asDouble() * 2.23694;
                    weatherForecast.setWindSpeed(speed);
                    maxWind = Math.max(maxWind, speed); // Convert m/s → mph
                }
                weatherForecastList.add(weatherForecast);

            }
            if(maxTemp > 40) {
                adviceList.add("Chance of high Temperature! Use sunscreen lotion");
            }
            if(maxWind > 10) {
                adviceList.add("Might be too windy, watch out!");
            }

            weatherResponse.setHighTemp(maxTemp);
            weatherResponse.setLowTemp(minTemp);
            weatherResponse.setAdvice(StringUtils.join(adviceList, " | "));
            weatherResponse.setWeatherForecastList(weatherForecastList);
            weatherResponseList.add(weatherResponse);
        }

        // 3. Return only next 3 days sorted

        return weatherResponseList.stream()
                .sorted(Comparator.comparing(WeatherResponse::getDate))
                .limit(3)
                .collect(Collectors.toList());
    }
}
