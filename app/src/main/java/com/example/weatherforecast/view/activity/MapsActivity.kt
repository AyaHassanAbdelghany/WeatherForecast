package com.example.weatherforecast.view.activity

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMapsBinding
import com.example.weatherforecast.viewmodel.FavWeatherViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.FavWeatherViewModelFactory
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.viewmodel.LocationViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.LocationViewModelFactory
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.repo.WeatherRepo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    //egypt
    private  var currentLocation :LatLng = LatLng(30.0595581,31.223445)
    private lateinit var geocoder: Geocoder
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationViewModelFactory: LocationViewModelFactory
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var favWeatherViewModel: FavWeatherViewModel
    private lateinit var favWeatherViewModelFactory: FavWeatherViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMapsBinding.inflate(layoutInflater)
     setContentView(binding.root)

       init()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnConfirm.setOnClickListener {

            val intent = intent.getBooleanExtra("Fav",false)
            if(intent) {
                homeViewModel.getWeatherOnline(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    homeViewModel.getUnitSharedPrefs(),
                    homeViewModel.getLocalizationSharedPref()
                )
                homeViewModel.baseWeather.observe(this@MapsActivity) {
                    favWeatherViewModel.insertFavWeather(it)
                    Thread.sleep(1000)
                    finish()
                }
            }
            else {
                locationViewModel.addLocationSharedPre(currentLocation)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
       val marker = mMap.addMarker(MarkerOptions().position(currentLocation)
            .draggable(true).title("Egypt"))!!
        marker.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(5f))


        // marker
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(location: Marker) {

            }

            override fun onMarkerDragEnd(location: Marker) {
                currentLocation = LatLng(location.position.latitude,location.position.longitude)
                val address  = homeViewModel.getCity(currentLocation)
                marker.title =address
                marker.showInfoWindow()
            }

            override fun onMarkerDragStart(p0: Marker) {
                marker.title=""
            }

        })

        //map
        mMap.setOnMapClickListener { location ->
            currentLocation = LatLng(location.latitude, location.longitude)
            marker.position = location
              val address = homeViewModel.getCity(currentLocation)
            marker.title = address
            marker.showInfoWindow()
        }
    }


    private fun init(){
        favWeatherViewModelFactory = FavWeatherViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(this)
                , SharedPrefs.getInstance(this),this))
        favWeatherViewModel = ViewModelProvider(this, favWeatherViewModelFactory)[FavWeatherViewModel::class.java]
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(this)
                , SharedPrefs.getInstance(this),this),this)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        geocoder= Geocoder(this, Locale.getDefault())
        locationViewModelFactory = LocationViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(this)
                , SharedPrefs.getInstance(this),this),this)
        locationViewModel = ViewModelProvider(this, locationViewModelFactory)[LocationViewModel::class.java]

    }
}