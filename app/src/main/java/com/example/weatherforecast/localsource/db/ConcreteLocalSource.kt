package com.example.weatherforecast.localsource.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.roomdemomvvm.db.AppDataBaseWeather
import com.example.roomdemomvvm.db.LocalSource
import com.example.weatherforecast.pojo.Alert
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
        dao.deleteWeather()
        dao.insertWeather(weather)

    }

    suspend override fun deleteFavWeather(weather: BaseWeather) {
       favDao.deleteFavWeather(weather)
    }

    override suspend fun getWeather(): BaseWeather {
       return dao.getWeather()
    }


    override fun insertAlertWeather(alert: Alert) {
        dao.insertAlertWeather(alert)
    }

    override suspend fun deleteAlertWeather(alert: Alert) {
        dao.deleteAlertWeather(alert)
    }

    override suspend fun getAlertWeather(): List<Alert> {
        return dao.getAlertWeather()
    }

    override fun insertFavWeather(weather: BaseWeather) {
        favDao.insertFavWeather(weather)
    }

    override suspend fun getFavWeather(): List<BaseWeather> {
        return favDao.getFavWeather()
    }
}