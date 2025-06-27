package com.weather.weather_prediction.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OpenWeatherClient {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;

    public OpenWeatherClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode getForecast(String city) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String fullUrl = String.format("%s?q=%s&appid=%s&cnt=24&units=metric",
                    apiUrl, URLEncoder.encode(city, StandardCharsets.UTF_8), apiKey);

            HttpGet request = new HttpGet(fullUrl);
            request.addHeader(HttpHeaders.ACCEPT, "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    HttpEntity entity = response.getEntity();
                    String responseBody = EntityUtils.toString(entity);
                    return objectMapper.readTree(responseBody);
                } else {
                    throw new RuntimeException("Failed to fetch forecast: " + statusCode);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenWeatherMap API", e);
        }
    }
}
