package com.example.weatherforecast.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.adapter.WeatherDailyAdapter
import com.example.weatherforecast.adapter.WeatherHourlyAdapter
import com.example.weatherforecast.constant.Constants
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.viewmodel.LocationViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.LocationViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val PERMISSION_ID=1
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationViewModelFactory: LocationViewModelFactory

    private lateinit var weatherDailyAdapter: WeatherDailyAdapter
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lang = homeViewModel.getLocalizationSharedPref()
        val config = resources.configuration
        val locale = Locale(lang)
        config.setLocale(locale)
        resources.updateConfiguration(config,resources.displayMetrics)

        homeViewModel.getUnitSharedPrefs()

        if(homeViewModel.checkForInternet()) {
            if (locationViewModel.getGpsOrMapSharedPre() == "GPS") {
                if(isLocationEnabled()){
                    if(!checkPermission()){
                        requestPermission()
                    }
                }
                else{
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                locationViewModel.getLocation()
                locationViewModel.location.observe(viewLifecycleOwner) {
                    locationViewModel.addLocationSharedPre(it)
                    homeViewModel.getWeatherOnline(it.latitude,
                        it.longitude,homeViewModel.getUnitSharedPrefs()
                        ,homeViewModel.getLocalizationSharedPref())
                }
            } else {
                val gson = Gson()
                val json: String = locationViewModel.getLocationSharedPre()
                val location: LatLng = gson.fromJson(json, LatLng::class.java)
                homeViewModel.getWeatherOnline(location.latitude,
                    location.longitude,homeViewModel.getUnitSharedPrefs()
                    ,homeViewModel.getLocalizationSharedPref())
            }
            homeViewModel.baseWeather.observe(viewLifecycleOwner){
                homeViewModel.insertWeather(it)
            }
        }
        else{
            val gson = Gson()
            val json: String = locationViewModel.getLocationSharedPre()
            val location: LatLng = gson.fromJson(json, LatLng::class.java)
            homeViewModel.getWeatherOffline(location)
        }

        homeViewModel.baseWeather.observe(viewLifecycleOwner){
            showCurrentWeather(it)
            weatherHourlyAdapter.setHours(it.hourly)
            binding.recyclerHours.adapter = weatherHourlyAdapter

            weatherDailyAdapter.setDays(it.daily)
            binding.recyclerDays.adapter = weatherDailyAdapter
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showCurrentWeather(_baseWeather: BaseWeather) {

        binding.txtLocation.text = _baseWeather.timezone
        binding.txtDate.text = FormatWeather.getFormat(_baseWeather.current.dt,Constants.formatDate)
        binding.cardview.txtTime.text = FormatWeather.getFormat(_baseWeather.current.dt,Constants.formatTime)
        binding.cardview.txtStatus.text = _baseWeather.current.weather.first().description
        binding.cardview.txtTemp.text = _baseWeather.current.temp.toString()+ HomeViewModel.weatherUnits.temp.unit
        val uri = FormatWeather.getIconFormat(_baseWeather.current.weather.first().icon)
        Picasso.get().load(uri).placeholder(R.drawable.clouds)
            .into(binding.cardview.imgStatus)
        binding.cardview.txtPressure.text = _baseWeather.current.pressure.toString()+ HomeViewModel.weatherUnits.pressure
        binding.cardview.txtHumidity.text = _baseWeather.current.humidity.toString() + HomeViewModel.weatherUnits.humidity
        binding.cardview.txtWind.text = _baseWeather.current.wind_speed.toString() + HomeViewModel.weatherUnits.windSpeed.unit
        binding.cardview.txtCloud.text = _baseWeather.current.clouds.toString()+ HomeViewModel.weatherUnits.clouds

    }

    private fun init(){

        weatherHourlyAdapter = WeatherHourlyAdapter()
        binding.recyclerHours.adapter = weatherHourlyAdapter
        weatherDailyAdapter = WeatherDailyAdapter()
        binding.recyclerDays.adapter = weatherDailyAdapter
        locationViewModelFactory = LocationViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(requireContext())
                , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        locationViewModel = ViewModelProvider(this, locationViewModelFactory)[LocationViewModel::class.java]


        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(WeatherClient.getInstance()
            ,ConcreteLocalSource.getInstance(requireContext())
            , SharedPrefs.getInstance(requireContext()),requireContext()))
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                requireActivity().finish()
            }
        }
    }


}