package com.example.weatherforecast.localsource.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.pojo.BaseWeather

@Dao
interface FavouriteWeatherDAO {

    @Query("SELECT * From weather ")
    suspend fun getFavWeather(): List<BaseWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavWeather(weather: BaseWeather)
}