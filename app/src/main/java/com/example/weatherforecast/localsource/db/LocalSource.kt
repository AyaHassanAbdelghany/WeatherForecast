package com.example.roomdemomvvm.db

import androidx.lifecycle.LiveData
import com.example.weatherforecast.pojo.BaseWeather
import com.google.android.gms.maps.model.LatLng


interface LocalSource {

    fun insertWeather(weather: BaseWeather)
    fun deleteWeather(weather: BaseWeather)
    suspend fun getWeather(lat :Double, lon :Double): BaseWeather

    fun insertFavWeather(weather: BaseWeather)
    suspend fun getFavWeather(): List<BaseWeather>

}