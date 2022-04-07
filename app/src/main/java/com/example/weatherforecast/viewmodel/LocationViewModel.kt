package com.example.weatherforecast.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.repo.RepoInterface
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class LocationViewModel (private var iRepo:RepoInterface,private var context:Context) :ViewModel(){

    private val _location : MutableLiveData<LatLng> = MutableLiveData()
    val  location: LiveData<LatLng> = _location
    private  var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    //egypt
    private  var currentLocation :LatLng = LatLng(30.0595581,31.223445)


    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                if (it == null) {
                    getFreshLocation()
                }
                else{
                    currentLocation = LatLng(it.latitude,it.longitude)
                    _location.postValue(currentLocation)
                }
            }
    }
    @SuppressLint("UseRequireInsteadOfGet", "MissingPermission")
    fun getFreshLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback : LocationCallback = object : LocationCallback(){
            override fun onLocationResult(onLocalResult: LocationResult?) {
                super.onLocationResult(onLocalResult)
                val loc : Location = onLocalResult!!.lastLocation
                currentLocation = LatLng(loc.latitude,loc.longitude)
                _location.postValue(currentLocation)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    fun addGpsOrMapSharedPre(gpsOrMap:String){
        iRepo.addGpsOrMapSharedPrefs(gpsOrMap)
    }
    fun getGpsOrMapSharedPre():String{
       return iRepo.getGpsOrMapSharedPrefs()
    }
    fun addLocationSharedPre(location:LatLng){
        iRepo.addLocationSharedPrefs(location)
    }
    fun getLocationSharedPre():String{
        return iRepo.getLocationSharedPrefs()
    }

}