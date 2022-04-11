package com.example.weatherforecast.pojo

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather", primaryKeys = ["lat","lon"])
data class BaseWeather(
    @SerializedName("lat")
    var lat:Double  ,
    @SerializedName("lon")
    var lon :Double,
    @SerializedName("timezone")
    var timezone :String,
    @SerializedName("timezone_offset")
    var timezone_offset :Double,
    @SerializedName("current")
    var current :Current,
    @SerializedName("hourly")
   var hourly :List<Hourly>,
    @SerializedName("daily")
   var daily :List<Daily>,
    @SerializedName("alerts")
    var alert: List<AlertWeather>?
    )