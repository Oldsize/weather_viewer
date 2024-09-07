package com.example.weather_project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherApiResponse {

    @JsonProperty("current_condition")
    private WeatherData[] currentCondition;

    public WeatherData getCurrentCondition() {
        return currentCondition != null && currentCondition.length > 0 ? currentCondition[0] : null;
    }
}
