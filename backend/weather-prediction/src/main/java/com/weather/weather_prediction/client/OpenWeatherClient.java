package com.weather.weather_prediction.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weather_prediction.dto.CachedWeather;
import com.weather.weather_prediction.dto.WeatherApiResponse;
import com.weather.weather_prediction.model.WeatherResponse;
import com.weather.weather_prediction.service.WeatherService;
import com.weather.weather_prediction.util.WeatherUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OpenWeatherClient {
    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;
    private final ObjectMapper objectMapper;

    public OpenWeatherClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode fetchAndTransform(String city) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String fullUrl = String.format("%s?q=%s&appid=%s&cnt=24&units=metric",
                    apiUrl, URLEncoder.encode(city, StandardCharsets.UTF_8), apiKey);

            HttpGet request = new HttpGet(fullUrl);
            request.addHeader(HttpHeaders.ACCEPT, "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                    String body = EntityUtils.toString(response.getEntity());
                    JsonNode node = objectMapper.readTree(body);
                    return node;
                } else {
                    throw new RuntimeException("API returned error: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather forecast", e);
        }
    }
}
