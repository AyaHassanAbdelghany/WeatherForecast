package com.example.weatherforecast.pojo

import com.example.weatherforecast.enum.Units

data class WeatherUnit(
    var temp :Units,
    var windSpeed :Units,
    var humidity:String="%",
    var pressure :String = "hpa",
    var clouds :String = "%"
)