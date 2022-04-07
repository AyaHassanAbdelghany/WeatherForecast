package com.example.weatherforecast.network

import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.pojo.BaseWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
 @GET("onecall?")
 suspend fun getWeather(@Query("lat") lat :Double,
                        @Query("lon") lon :Double,
                        @Query("appid") apiKey :String,
                        @Query("units") unit :String,
                       @Query("lang") lang :String
 ):BaseWeather

}