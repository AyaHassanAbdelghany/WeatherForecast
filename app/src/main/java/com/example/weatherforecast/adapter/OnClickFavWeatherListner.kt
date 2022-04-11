package com.example.weatherforecast.adapter

import com.example.weatherforecast.pojo.BaseWeather

interface OnClickFavWeatherListner {
    fun onClickDelete(baseWeather: BaseWeather)
    fun onClickDetails(baseWeather:BaseWeather)
}