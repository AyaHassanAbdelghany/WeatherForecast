package com.example.roomdemomvvm.db

import android.content.Context
import androidx.room.*
import kotlin.jvm.Synchronized
import com.example.weatherapp.model.DataBaseConvert
import com.example.weatherforecast.localsource.db.WeatherDAO
import com.example.weatherforecast.pojo.BaseWeather

@Database(entities = [BaseWeather::class], version = 1)
@TypeConverters(DataBaseConvert::class)
abstract class AppDataBaseWeather : RoomDatabase() {
    abstract fun weatherDAO(): WeatherDAO

    companion object {
        private var instance: AppDataBaseWeather? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBaseWeather{
            return instance?: Room.databaseBuilder(
                context.applicationContext,
                AppDataBaseWeather::class.java,
                "weatherForecast"
            ).fallbackToDestructiveMigration().build()
        }
    }
}