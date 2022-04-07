package com.example.weatherforecast.repo

import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.pojo.BaseWeather
import com.google.android.gms.maps.model.LatLng
import java.util.*

interface RepoInterface {



    fun getLocalizationDevice():String

    //connection
    fun checkForInternet():Boolean

    //network
    suspend fun getWeatherOnline(lat :Double, lon :Double, unit :Units, lang:String):BaseWeather

    //shared
    fun addGpsOrMapSharedPrefs(locaton:String)
    fun getGpsOrMapSharedPrefs():String
    fun addLocationSharedPrefs(location:LatLng)
    fun getLocationSharedPrefs():String
    fun addUnitSharedPrefs(unit :Units)
    fun getUnitSharedPrefs():String
    fun addLocalizationSharedPref(locale:String)
    fun getLocalizationSharedPref():String

    //room
     fun insertWeather(weather:BaseWeather)
    suspend fun getWeatherOffline(lat :Double, lon :Double):BaseWeather
    fun insertFavWeather(weather:BaseWeather)
    suspend fun getFavWeatherOffline():List<BaseWeather>

}