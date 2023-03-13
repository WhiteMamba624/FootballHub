package com.gligamihai.footballhub.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {
    @SerializedName("visibility")
    public int visibility;

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @SerializedName("main")
    private Weather weather;

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @SerializedName("wind")
    private Wind wind;

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @SerializedName("weather")
    private List<Main> weatherList;

    public List<Main> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Main> weatherList) {
        this.weatherList = weatherList;
    }
}
