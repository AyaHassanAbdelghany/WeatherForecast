package com.example.weatherforecast.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {


    companion object {
        private  var retrofit: Retrofit?=null
        fun getClient(): Retrofit {
            return retrofit ?: Retrofit.Builder()
                .baseUrl(WeatherClient.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
    }
}