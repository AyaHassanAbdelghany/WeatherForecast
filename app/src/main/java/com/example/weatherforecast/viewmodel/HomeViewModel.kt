package com.example.weatherforecast.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.opengl.GLES10
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.R
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.pojo.WeatherUnit
import com.example.weatherforecast.repo.RepoInterface
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class HomeViewModel(iRepo :RepoInterface,var context:Context) : ViewModel() {

    private val _iRepo :RepoInterface = iRepo
    private val _baseWeather = MutableLiveData<BaseWeather>()
    val baseWeather : LiveData<BaseWeather> = _baseWeather

    companion object{
        lateinit var weatherUnits:WeatherUnit
        fun setWeatherUnit(unit: Units,context:Context){
            when (unit.name) {
                Units.Metric.name -> weatherUnits= WeatherUnit(context.getString(R.string.Celsius),
                    context.getString(R.string.MeterPerSec),context.getString(R.string.pressure))
                Units.Imperial.name ->weatherUnits = WeatherUnit(context.getString(R.string.Fahrenheit),
                    context.getString(R.string.MilesPerHour),context.getString(R.string.pressure))
                Units.Standard.name -> weatherUnits = WeatherUnit(context.getString(R.string.Kelvin),
                    context.getString(R.string.MeterPerSec),context.getString(R.string.pressure))
            }
        }
    }


    fun getCity(loc: LatLng): String{
        var local = Locale( getLocalizationSharedPref())
        val geocoder = Geocoder(context,local)
        val addresses: List<Address> = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
        return addresses[0].countryName
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
        setWeatherUnit(unit,context)
        return unit
    }

    fun addLocalizationSharedPref(locale: String){
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
            withContext(Dispatchers.Main){
            }
        }
        }
     fun getWeatherOffline(){
        viewModelScope.launch {
            val weather =  _iRepo.getWeatherOffline()
            withContext(Dispatchers.Main){
                _baseWeather.postValue(weather)
            }
        }
    }
}