package com.gligamihai.footballhub.Utils;

import com.gligamihai.footballhub.BuildConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather?APPID="+ BuildConfig.weatherApiKey+"&units=metric")
    Call<WeatherData> getWeatherData(@Query("q") String cityName);
}
