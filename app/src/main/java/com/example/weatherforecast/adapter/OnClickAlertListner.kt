package com.example.weatherforecast.adapter

import com.example.weatherforecast.pojo.Alert

interface OnClickAlertListner {
    fun onClick(alert: Alert,position:Int)

}