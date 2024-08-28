package com.example.weather_project.exceptions;

public class WeatherNotFoundException extends Exception {
    public WeatherNotFoundException() {
        super("Weather Not Found");
    }
}
