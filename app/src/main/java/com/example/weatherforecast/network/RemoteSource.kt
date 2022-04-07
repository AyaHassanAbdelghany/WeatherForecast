package com.example.weatherforecast.network

import com.example.weatherforecast.pojo.BaseWeather

interface RemoteSource {

    suspend fun getWeather(lat:Double , lon:Double,  unit :String, lang :String) :BaseWeather
}