package com.example.weatherapp.model

import androidx.room.TypeConverter
import com.example.weatherforecast.pojo.*
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
    fun stringToObjectList(data: String?): List<String> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson<List<String>>(
            data,
            listType
        )
    }

    @TypeConverter
    fun objectListToString(data: List<String?>?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<String?>?>() {}.type
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
    fun stringAlertToObjectList(data: String?): List<AlertWeather> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<AlertWeather?>?>() {}.type
        return gson.fromJson<List<AlertWeather>>(
            data,
            listType
        )
    }

    @TypeConverter
    fun objectAlertListToString(data: List<AlertWeather?>?): String? {
        if (data == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<AlertWeather?>?>() {}.type
        return gson.toJson(data, type)
    }



    companion object {
        var gson = Gson()
    }
}
