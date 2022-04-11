package com.example.weatherforecast.localsource.db

import androidx.room.*
import com.example.weatherforecast.pojo.BaseWeather

@Dao
interface FavouriteWeatherDAO {

    @Query("SELECT * From weather ")
    suspend fun getFavWeather(): List<BaseWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavWeather(weather: BaseWeather)

    @Delete
    suspend fun deleteFavWeather(weather:BaseWeather)
}