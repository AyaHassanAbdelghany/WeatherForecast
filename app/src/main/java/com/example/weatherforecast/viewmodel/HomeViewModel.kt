package com.example.weatherforecast.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.pojo.WeatherUnit
import com.example.weatherforecast.repo.RepoInterface
import com.example.weatherforecast.view.HomeFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(iRepo :RepoInterface) : ViewModel() {

    private val _iRepo :RepoInterface = iRepo
    private val _baseWeather = MutableLiveData<BaseWeather>()
    val baseWeather : LiveData<BaseWeather> = _baseWeather

    companion object{
        lateinit var weatherUnits:WeatherUnit
        fun setWeatherUnit(unit: Units){
            when (unit.name) {
                Units.Metric.name -> weatherUnits= WeatherUnit(Units.Celsius, Units.MeterPerSec)
                Units.Imperial.name ->weatherUnits = WeatherUnit(Units.Fahrenheit, Units.MilesPerHour)
                Units.Standard.name -> weatherUnits = WeatherUnit(Units.Kelvin,Units.MeterPerSec)
            }
        }
    }



    fun getLocalizationDevice():String{
        return _iRepo.getLocalizationDevice()
    }
    //connection
    fun checkForInternet():Boolean{
       return _iRepo.checkForInternet()
    }

    //shared
    fun addUnitSharedPrefs(unit: Units){
        _iRepo.addUnitSharedPrefs(unit)

    }
    fun getUnitSharedPrefs():Units{
        val json =  _iRepo.getUnitSharedPrefs()
        val gson = Gson()
       val unit =  gson.fromJson(json, Units::class.java)
        setWeatherUnit(unit)
        return unit
    }

    fun addLocalizationSharedPref(locale:String){
        _iRepo.addLocalizationSharedPref(locale)
    }
    fun getLocalizationSharedPref():String{
        return _iRepo.getLocalizationSharedPref()
    }

    //network
    fun getWeatherOnline(lat :Double ,lon :Double, unit :Units, lang :String){
        viewModelScope.launch {
           val weather =  _iRepo.getWeatherOnline(lat,lon,unit, lang)
            withContext(Dispatchers.Main){
                _baseWeather.postValue(weather)
            }
        }
    }

    //room
     fun insertWeather(weather :BaseWeather){
       val latLon =  FormatWeather.getFormatLatLon(weather.lat,weather.lon)
        weather.lat= latLon.latitude
        weather.lon = latLon.longitude
        viewModelScope.launch(Dispatchers.IO) {
            _iRepo.insertWeather(weather)
        }
        }
     fun getWeatherOffline(location:LatLng){
        viewModelScope.launch {
           val latLon =  FormatWeather.getFormatLatLon(location.latitude,location.longitude)
            val weather =  _iRepo.getWeatherOffline(latLon.latitude,latLon.longitude)
            withContext(Dispatchers.Main){
                _baseWeather.postValue(weather)
            }
        }
    }
}