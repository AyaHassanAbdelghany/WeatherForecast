package com.example.weatherforecast.localsource.db


import androidx.room.*
import com.example.weatherforecast.pojo.Alert
import com.example.weatherforecast.pojo.BaseWeather


@Dao
interface WeatherDAO {

    @Query("SELECT * From weather LIMIT 1")
    suspend fun getWeather(): BaseWeather
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(weather: BaseWeather)
    @Query("DELETE FROM weather ")
     fun deleteWeather()




    @Query("SELECT * From weatherAlert ")
    suspend fun getAlertWeather(): List<Alert>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlertWeather(alert :Alert)
    @Delete
    suspend fun deleteAlertWeather(alert:Alert)


}