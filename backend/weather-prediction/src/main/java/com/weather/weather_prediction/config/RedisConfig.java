package com.weather.weather_prediction.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weather_prediction.dto.CachedWeather;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CachedWeather> redisTemplate(RedisConnectionFactory factory, ObjectMapper mapper) {

        RedisTemplate<String, CachedWeather> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<CachedWeather> valueSerializer = new Jackson2JsonRedisSerializer<>(CachedWeather.class);
        valueSerializer.setObjectMapper(mapper);
        template.setValueSerializer(valueSerializer);
        return template;
    }
}
