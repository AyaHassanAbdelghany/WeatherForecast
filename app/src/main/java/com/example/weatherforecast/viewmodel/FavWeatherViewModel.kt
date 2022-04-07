package com.example.weatherforecast.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.repo.RepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavWeatherViewModel(iRepo : RepoInterface) : ViewModel() {

    private val _iRepo :RepoInterface = iRepo
    private val _favWeather = MutableLiveData<List<BaseWeather>>()
    val favWeather : LiveData<List<BaseWeather>> = _favWeather

    //room
    fun insertFavWeather(weather : BaseWeather){
        val latLon =  FormatWeather.getFormatLatLon(weather.lat,weather.lon)
        weather.lat= latLon.latitude
        weather.lon = latLon.longitude
        viewModelScope.launch(Dispatchers.IO) {
            _iRepo.insertFavWeather(weather)
        }
    }
    fun getFavWeather(){
        viewModelScope.launch {
        val weather = _iRepo.getFavWeatherOffline()
            withContext(Dispatchers.Main){
                _favWeather.postValue(weather)
            }
        }
    }
    fun deleteFavWeather(){

    }
}