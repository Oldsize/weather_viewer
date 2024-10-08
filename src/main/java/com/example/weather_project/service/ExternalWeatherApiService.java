package com.example.weather_project.service;

import com.example.weather_project.exceptions.WeatherNotFoundException;
import com.example.weather_project.model.WeatherApiResponse;
import com.example.weather_project.model.WeatherData;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ExternalWeatherApiService {

    public WeatherData getWeather(String city) throws WeatherNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String wttrUrl = "https://wttr.in/";
        String url = wttrUrl + city + "?format=j2";
        try {
            WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);
            assert response != null;
            return response.getCurrentCondition();
        } catch (RestClientException e) {
            throw new WeatherNotFoundException();
        }
    }
}