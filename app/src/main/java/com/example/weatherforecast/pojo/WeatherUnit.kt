package com.example.weatherforecast.pojo


data class WeatherUnit(
    var temp :String,
    var windSpeed :String,
    var pressure :String ,
    var humidity:String="%",
    var clouds :String = "%"
)