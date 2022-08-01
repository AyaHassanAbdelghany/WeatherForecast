package com.example.weatherforecast.constant

import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*

class FormatWeather {

    companion object{

        fun getFormat(date :Int,format :String,lang:String):String{
            val date = Date(date * 1000L)
            var locale = Locale(lang)
            val jdf = SimpleDateFormat(format,locale)
            val _date: String = jdf.format(date)
            return _date
        }
        fun getIconFormat(icon:String): String{
            return Constants.baseUriImg+icon+ Constants.format_baseUriImg
        }
        fun getFormatLatLon(lat :Double,lon :Double):LatLng{
            return (LatLng(String.format(Locale(Locale.ENGLISH.toString()),"%.3f",lat).toDouble(),
               String.format(Locale(Locale.ENGLISH.toString()),"%.3f",lon).toDouble()))
        }
    }
}