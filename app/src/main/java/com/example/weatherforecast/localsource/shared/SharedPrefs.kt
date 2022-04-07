package com.example.weatherforecast.localsource.shared

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherforecast.enum.Units
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson




class SharedPrefs private constructor(): SharedPrefsInterface {

     companion object {
        private var sharedPref: SharedPrefs = SharedPrefs()
        private lateinit var sharedPreferences: SharedPreferences
        private var editor: SharedPreferences.Editor? = null

        fun getInstance(context: Context): SharedPrefs {
            if (!Companion::sharedPreferences.isInitialized) {
                synchronized(SharedPrefs::class.java) {
                    if (!Companion::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences("Weather", Context.MODE_PRIVATE)
                        editor =  sharedPreferences.edit()
                    }
                }
            }
            return sharedPref
        }
    }

    override fun addUnitSharedPre(unit:Units) {
        val gson = Gson()
        val json = gson.toJson(unit)
        editor?.putString("Unit", json)?.apply()
    }


    override fun getUnitSharedPre():String {
        return sharedPreferences
            .getString("Unit",Units.Standard.name)!!
    }

    override fun addGpsOrMapSharedPre(location: String) {
        editor?.putString("Location", location)?.apply()
    }

    override fun getGpsOrMapSharedPre(): String {
        return sharedPreferences
            .getString("Location","")!!
    }

    override fun addLocationSharedPre(location: LatLng) {
        val gson = Gson()
        val json = gson.toJson(location)
       editor?.putString("LatLong", json)?.apply()
    }

    override fun getLocationSharedPre(): String{
        return sharedPreferences.getString("LatLong","")!!
    }

    override fun addLocalizationSharedPref(locale: String) {
        editor?.putString("Lang", locale)?.apply()
    }

    override fun getLocalizationSharedPref(): String {
        return sharedPreferences.getString("Lang","")!!
    }

    override fun deleteSharedPre() {
    }
}


