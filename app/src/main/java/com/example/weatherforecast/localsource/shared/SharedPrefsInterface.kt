package com.example.weatherforecast.localsource.shared

import com.example.weatherforecast.enum.Units
import com.google.android.gms.maps.model.LatLng

interface SharedPrefsInterface {

    fun addUnitSharedPre(unit:Units)
    fun getUnitSharedPre():String
    fun addGpsOrMapSharedPre(locaton:String)
    fun getGpsOrMapSharedPre():String
    fun addLocationSharedPre(location:LatLng)
    fun getLocationSharedPre():String
    fun addLocalizationSharedPref(locale:String)
    fun getLocalizationSharedPref():String
    fun deleteSharedPre()
}