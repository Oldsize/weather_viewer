package com.example.weather_project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

    @JsonProperty("FeelsLikeC")
    private double feelsLikeC;

    @JsonProperty("temp_C")
    private double tempC;

    @JsonProperty("pressure")
    private int pressure;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("windspeedKmph")
    private double windSpeedKmPh;

    @JsonProperty("winddirDegree")
    private int windDirDegree;

    @JsonProperty("lang_ru")
    private List<LangValue> langRu;

    @JsonProperty("latitude")
    private BigDecimal latitude;
    @JsonProperty("longitude")
    private BigDecimal longitude;
}