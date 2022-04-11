package com.example.weatherforecast.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.adapter.WeatherDailyAdapter
import com.example.weatherforecast.adapter.WeatherHourlyAdapter
import com.example.weatherforecast.constant.Constants
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.databinding.ActivityFavouriteDetailsBinding
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.math.roundToInt

class FavouriteDetailsActivity : AppCompatActivity() {

    private lateinit var binding :ActivityFavouriteDetailsBinding
    private lateinit var weatherDailyAdapter: WeatherDailyAdapter
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var  lang :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteDetailsBinding.inflate(layoutInflater)
        init()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        lang = homeViewModel.getLocalizationSharedPref()
        val gson = Gson()
        val json: String = intent.getStringExtra("object")!!
        val weather: BaseWeather = gson.fromJson(json, BaseWeather::class.java)
        showCurrentWeather(weather)
        weatherHourlyAdapter.setHours(weather.hourly)
        binding.recyclerHours.adapter = weatherHourlyAdapter
        weatherDailyAdapter.setDays(weather.daily)
        binding.recyclerDays.adapter = weatherDailyAdapter

    }
    private fun showCurrentWeather(baseWeather: BaseWeather) {
        binding.txtLocation.text = homeViewModel.getCity(LatLng(baseWeather.lat,baseWeather.lon))
        binding.txtDate.text = FormatWeather.getFormat(baseWeather.current.dt, Constants.formatDate,lang)

        binding.cardview.txtTime.text = FormatWeather.getFormat(baseWeather.current.dt, Constants.formatTime,lang)

        binding.cardview.txtStatus.text = baseWeather.current.weather.first().description

        binding.cardview.txtTemp.text = "${
            String.format(
                Locale(lang),"%d",
                baseWeather.current.temp.toInt())} ${
            HomeViewModel.weatherUnits.temp}"

        val uri = FormatWeather.getIconFormat(baseWeather.current.weather.first().icon)
        Picasso.get().load(uri).placeholder(R.drawable.clouds)
            .into(binding.cardview.imgStatus)

        binding.cardview.txtPressure.text =
            "${
                String.format(Locale(lang),"%d",
                    baseWeather.current.pressure.toInt())}  ${
                HomeViewModel.weatherUnits.pressure}"

        binding.cardview.txtHumidity.text =
            "${
                String.format(Locale(lang),"%d",
                    baseWeather.current.humidity.toInt())}  ${
                HomeViewModel.weatherUnits.humidity}"

        binding.cardview.txtWind.text =
            "${
                String.format(Locale(lang),"%d",
                    baseWeather.current.wind_speed.toInt())} ${
                HomeViewModel.weatherUnits.windSpeed}"

        binding.cardview.txtCloud.text =

            "${
                String.format(Locale(lang),"%d",
                    baseWeather.current.clouds.toInt())} ${
                HomeViewModel.weatherUnits.clouds}"
    }


    private fun init(){

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
            , ConcreteLocalSource.getInstance(this)
            , SharedPrefs.getInstance(this),this),this)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

        lang = homeViewModel.getLocalizationSharedPref()

        weatherHourlyAdapter = WeatherHourlyAdapter(lang)
        binding.recyclerHours.adapter = weatherHourlyAdapter
        weatherDailyAdapter = WeatherDailyAdapter(lang)
        binding.recyclerDays.adapter = weatherDailyAdapter
    }
}