package com.example.weatherforecast.network

import com.example.weatherforecast.pojo.BaseWeather

class WeatherClient private constructor():RemoteSource{

    var apiKey :String = "98b3e64a3f1086083561278734496b12"

    override suspend fun getWeather(lat: Double, lon: Double, unit: String, lang:String): BaseWeather {
        val weatherService  : WeatherService =RetrofitHelper.getClient()
            .create(WeatherService::class.java)
         var response = weatherService.getWeather(lat,lon,apiKey, unit, lang)
        return  response
    }

    companion object {
        var baseUrl: String = "https://api.openweathermap.org/data/2.5/"
        private var weatherClient: WeatherClient? = null
        fun getInstance(): WeatherClient {
            return weatherClient ?: WeatherClient()
        }

    }
}