package com.example.roomdemomvvm.db

import com.example.weatherforecast.pojo.Alert
import com.example.weatherforecast.pojo.BaseWeather


interface LocalSource {

    fun insertWeather(weather: BaseWeather)
    suspend fun getWeather(): BaseWeather


    fun insertAlertWeather(alert: Alert)
    suspend fun deleteAlertWeather(alert:Alert)
    suspend fun getAlertWeather(): List<Alert>


    fun insertFavWeather(weather: BaseWeather)
    suspend fun getFavWeather(): List<BaseWeather>
    suspend fun deleteFavWeather(weather: BaseWeather)


}