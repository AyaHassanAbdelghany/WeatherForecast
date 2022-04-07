package com.example.weatherforecast.localsource.db


import androidx.room.*
import com.example.weatherforecast.pojo.BaseWeather


@Dao
interface WeatherDAO {

    @Query("SELECT * From weather WHERE lat = (:lat) AND lon = (:lon)")
    suspend fun getWeather(lat :Double,lon:Double): BaseWeather

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weather: BaseWeather)

}