package com.example.weatherforecast.enum

enum class Units (val unit :String){

    Metric("metric"),       // C
    Standard("standard"),   // K
    Imperial("imperial") ,  //F
    MeterPerSec("Meter/Sec"), //C,K
    MilesPerHour("Miles/Hour"), //F
    Fahrenheit("°F"),
    Kelvin("°K"),
    Celsius("°C"),
    Arabic("ar"),
    English("en")

}