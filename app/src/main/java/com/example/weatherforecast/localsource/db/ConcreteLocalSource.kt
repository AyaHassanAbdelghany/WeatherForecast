package com.example.weatherforecast.localsource.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.roomdemomvvm.db.AppDataBaseWeather
import com.example.roomdemomvvm.db.LocalSource
import com.example.weatherforecast.pojo.BaseWeather

class ConcreteLocalSource  : LocalSource {

    private  var dao: WeatherDAO
    private  var favDao: FavouriteWeatherDAO

    private constructor(context: Context){
        val db: AppDataBaseWeather = AppDataBaseWeather.getInstance(context)
        dao = db.weatherDAO()
        val dbFav: AppDataBaseFavouriteWeather = AppDataBaseFavouriteWeather.getInstance(context)
        favDao = dbFav.favWeatherDAO()
    }

    companion object{
        private var concreteLocalSource:ConcreteLocalSource?=null

        fun getInstance(context: Context):ConcreteLocalSource{
            return concreteLocalSource?:ConcreteLocalSource(context)
        }
    }


    override fun insertWeather(weather: BaseWeather) {
        dao.insertWeather(weather)

    }

    override fun deleteWeather(weather: BaseWeather) {

    }

    override suspend fun getWeather(lat :Double,lon :Double): BaseWeather {
       return dao.getWeather(lat,lon)
    }

    override fun insertFavWeather(weather: BaseWeather) {
        favDao.insertFavWeather(weather)
    }

    override suspend fun getFavWeather(): List<BaseWeather> {
        return favDao.getFavWeather()
    }
}