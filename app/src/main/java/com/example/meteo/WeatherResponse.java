package com.example.meteo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherResponse {
    @SerializedName("main")
    private MainData main;
    @SerializedName("weather")
    private List<Weather> weather;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("name")
    private String name;

    public MainData getMain() {
        return main;
    }
    public List<Weather> getWeather() { return weather; }
    public Wind getWind() { return wind; }

    public String getName() { return name; }

}

