package com.example.weatherforecast.repo

import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.pojo.Alert
import com.example.weatherforecast.pojo.AlertWeather
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
    suspend fun getWeatherOffline():BaseWeather

    fun insertFavWeather(weather:BaseWeather)
    suspend fun getFavWeather():List<BaseWeather>
    suspend fun deleteFavWeather(weather:BaseWeather)

    fun insertAlertWeather(alert:Alert)
    suspend fun getAlertWeather():List<Alert>
    suspend fun deleteAlertWeather(alert:Alert)

}