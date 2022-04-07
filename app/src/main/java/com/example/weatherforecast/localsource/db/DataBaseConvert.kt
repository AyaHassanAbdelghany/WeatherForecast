package com.example.weatherapp.model

import androidx.room.TypeConverter
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.pojo.Current
import com.example.weatherforecast.pojo.Daily
import com.example.weatherforecast.pojo.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class DataBaseConvert : Serializable {

    @TypeConverter
    fun stringDailyToObjectList(data: String?): List<Daily> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily>>(
            data,
            listType
        )
    }

    @TypeConverter
    fun objectDailyListToString(data: List<Daily?>?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun stringHourlyToObjectList(data: String?): List<Hourly> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly>>(
            data,
            listType
        )
    }

    @TypeConverter
    fun objectHourlyListToString(data: List<Hourly?>?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun stringToObject(data: String?): Current? {
        if (data == null) {
            return null
        }
        val listType = object : TypeToken<Current?>() {}.type
        return gson.fromJson<Current>(
            data,
            listType
        )
    }

    @TypeConverter
    fun objectToString(data: Current?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<Current?>() {}.type
        return gson.toJson(data, type)
    }


    @TypeConverter
    fun stringBaseWeatherToObjectList(data: String?): List<BaseWeather> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<BaseWeather?>?>() {}.type
        return gson.fromJson<List<BaseWeather>>(
            data,
            listType
        )
    }

    @TypeConverter
    fun objectBaseWeatherListToString(data: List<BaseWeather?>?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<BaseWeather?>?>() {}.type
        return gson.toJson(data, type)
    }

    companion object {
        var gson = Gson()
    }
}
