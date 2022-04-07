package com.example.weatherforecast.repo

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.roomdemomvvm.db.LocalSource
import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.localsource.shared.SharedPrefsInterface
import com.example.weatherforecast.network.RemoteSource
import com.example.weatherforecast.pojo.BaseWeather
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.util.*

class WeatherRepo  private constructor(
    private var remoteSource: RemoteSource,
    private var localSource: LocalSource,
    private var sharedPrefs: SharedPrefsInterface,
    var context: Context)
    :RepoInterface {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var weatherRepo :WeatherRepo ?=null

        fun getInstance(remoteSource: RemoteSource,localSource: LocalSource, sharedPrefs: SharedPrefsInterface, context: Context) :WeatherRepo{
            return weatherRepo?:WeatherRepo(remoteSource,localSource,sharedPrefs ,context)
        }
    }

    override suspend fun getWeatherOnline(lat: Double, lon: Double, unit :Units, lang :String): BaseWeather {

        return remoteSource.getWeather(lat, lon, unit.name, lang)
    }

    override suspend fun getWeatherOffline(lat:Double, lon: Double): BaseWeather {
        return localSource.getWeather(lat,lon)
    }

    override fun insertFavWeather(weather: BaseWeather) {
        localSource.insertFavWeather(weather)
    }

    override suspend fun getFavWeatherOffline(): List<BaseWeather> {
        return localSource.getFavWeather()
    }

    override fun addGpsOrMapSharedPrefs(locaton: String) {
        sharedPrefs.addGpsOrMapSharedPre(locaton)
    }

    override fun getGpsOrMapSharedPrefs(): String {
        return sharedPrefs.getGpsOrMapSharedPre()
    }

    override fun addLocationSharedPrefs(location: LatLng) {
        sharedPrefs.addLocationSharedPre(location)
    }

    override fun getLocationSharedPrefs(): String {
        return sharedPrefs.getLocationSharedPre()
    }

    override fun addUnitSharedPrefs(unit: Units) {
        sharedPrefs.addUnitSharedPre(unit)
    }

    override fun getUnitSharedPrefs(): String {
        return sharedPrefs.getUnitSharedPre()
    }

    override fun addLocalizationSharedPref(locale: String) {
        sharedPrefs.addLocalizationSharedPref(locale)
    }

    override fun getLocalizationSharedPref(): String {
        return sharedPrefs.getLocalizationSharedPref()
    }

    override  fun insertWeather(weather: BaseWeather) {
        localSource.insertWeather(weather)
    }

    override fun getLocalizationDevice(): String {
        return Locale.getDefault().getLanguage()
    }


    override fun checkForInternet(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->true

            else -> false
        }
    }

}