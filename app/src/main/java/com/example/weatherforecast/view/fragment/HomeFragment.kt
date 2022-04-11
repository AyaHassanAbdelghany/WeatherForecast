package com.example.weatherforecast.view.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.weatherforecast.view.activity.MainActivity
import com.example.weatherforecast.viewmodel.LocationViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.LocationViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationViewModelFactory: LocationViewModelFactory
    private lateinit var  lang :String
    private lateinit var weatherDailyAdapter: WeatherDailyAdapter
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

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

        homeViewModel.getUnitSharedPrefs()

        if (homeViewModel.checkForInternet()) {
            if (locationViewModel.getGpsOrMapSharedPre() == "GPS") {
                if(isLocationEnabled()){
                    checkPermissions()
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
                homeViewModel.getWeatherOnline(
                    location.latitude,
                    location.longitude,
                    homeViewModel.getUnitSharedPrefs(),
                    homeViewModel.getLocalizationSharedPref()
                )
            }
                homeViewModel.baseWeather.observe(viewLifecycleOwner) {
                    with(homeViewModel) {
                        insertWeather(it)
                        showCurrentWeather(it)
                        weatherDailyAdapter.setDays(it.daily)
                        binding.recyclerDays.adapter = weatherDailyAdapter
                        weatherHourlyAdapter.setHours(it.hourly)
                        binding.recyclerHours.adapter = weatherHourlyAdapter
                    }
                }
            } else {
                homeViewModel.getWeatherOffline()
            }
        }

    override fun onResume() {
        super.onResume()
        lang = homeViewModel.getLocalizationSharedPref()
        locationViewModel.getLocation()
    }


    private fun showCurrentWeather(baseWeather: BaseWeather) {

        binding.txtLocation.text = homeViewModel.getCity(LatLng(baseWeather.lat,baseWeather.lon))

        binding.txtDate.text = FormatWeather.getFormat(baseWeather.current.dt,Constants.formatDate,lang)

        binding.cardview.txtTime.text =FormatWeather.getFormat(baseWeather.current.dt,Constants.formatTime,lang)

        binding.cardview.txtStatus.text = baseWeather.current.weather.first().description

        binding.cardview.txtTemp.text ="${
            String.format(Locale(lang),"%d",
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

        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(WeatherClient.getInstance()
            ,ConcreteLocalSource.getInstance(requireContext())
            , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

        lang = homeViewModel.getLocalizationSharedPref()

        weatherHourlyAdapter = WeatherHourlyAdapter(lang)
        binding.recyclerHours.adapter = weatherHourlyAdapter
        weatherDailyAdapter = WeatherDailyAdapter(lang)
        binding.recyclerDays.adapter = weatherDailyAdapter
        locationViewModelFactory = LocationViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(requireContext())
                , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        locationViewModel = ViewModelProvider(this, locationViewModelFactory)[LocationViewModel::class.java]


    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                    }
                } else {
                    startActivity(Intent(activity, MainActivity::class.java))
                }
                return
            }
        }
    }
}