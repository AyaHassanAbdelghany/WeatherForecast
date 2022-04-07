package com.example.weatherforecast.localsource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.roomdemomvvm.db.AppDataBaseWeather
import com.example.weatherapp.model.DataBaseConvert
import com.example.weatherforecast.pojo.BaseWeather


@Database(entities = [BaseWeather::class], version = 1)
@TypeConverters(DataBaseConvert::class)
abstract class AppDataBaseFavouriteWeather : RoomDatabase() {
    abstract fun favWeatherDAO(): FavouriteWeatherDAO

    companion object {
        private var instance: AppDataBaseFavouriteWeather? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBaseFavouriteWeather {
            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDataBaseFavouriteWeather::class.java,
                "weatherForecastFav"
            ).fallbackToDestructiveMigration().build()
        }
    }
}