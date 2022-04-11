package com.example.weatherforecast.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.weatherforecast.constant.Constants
import com.example.weatherforecast.pojo.Alert
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.repo.RepoInterface
import com.example.weatherforecast.services.MyWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class AlertViewModel (var iRepo :RepoInterface,var context:Context) : ViewModel() {

    private val myCalendar = Calendar.getInstance()
    private var sdf = SimpleDateFormat(Constants.formatDate2)
    private lateinit var days: MutableList<String>
    private val _alertWeather = MutableLiveData<List<Alert>>()
    val alertWeather : LiveData<List<Alert>> = _alertWeather

    fun setDaysAlert( currentDate:String ,duration: Int): List<String> {
        days  = mutableListOf()
        myCalendar.time = sdf.parse(currentDate)
        days.add(sdf.format(myCalendar.time))
        for (i in 1..duration) {
            myCalendar.add(Calendar.DAY_OF_MONTH, 1)
            days.add(sdf.format(myCalendar.time))
        }
        return days
    }
    fun createListRequests(alerts: List<String>, time: String) {

        val requests: MutableList<WorkRequest> = mutableListOf()
        val currentTime: String = SimpleDateFormat(Constants.formatTime).format(Date())
        //val sdf = SimpleDateFormat(Constants.formatTime)
        //val currentTime = sdf.format(myCalendar.time)
        val currentTimeSplit: Array<String> = currentTime.split(":".toRegex()).toTypedArray()
        val timeSplit: Array<String> = time.split(":".toRegex()).toTypedArray()

        val delay =calDelay(currentTimeSplit,timeSplit)
        requests.add(createRequestsWorkRequest(delay))

        for (i in 0 until alerts.size-1) {
            requests.add(createRequestsWorkRequest(delay+(24*60)))
        }
        WorkManager.getInstance(context).enqueue(requests)
    }
    private fun createRequestsWorkRequest(delay: Int): WorkRequest {
        Log.i("TAG", "createRequestsWorkRequest: ${_alertWeather.value?.size.toString()}")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(delay.toLong(), TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(_alertWeather.value?.size.toString())
            .build()
    }
    private fun calDelay(currentTime: Array<String>, time: Array<String>): Int {
        val currentTimeHour: String = currentTime[0]
        val currentTimeMinute: String = currentTime[1]
        val timeHour: String = time[0]
        val timeMinute: String = time[1]
        val diffH: Int =
            abs(Integer.parseInt(currentTimeHour.trim()) - Integer.parseInt(timeHour.trim()))
        val diffM: Int =
            abs(Integer.parseInt(currentTimeMinute.trim()) - Integer.parseInt(timeMinute.trim()))
        return if (diffH >= 1) diffH * 60 + diffM else diffH + diffM
    }

    fun insertAlert(alert:Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            iRepo.insertAlertWeather(alert)
        }
    }

    fun getAlertWeather(){
        viewModelScope.launch {
            val alert =  iRepo.getAlertWeather()
            withContext(Dispatchers.Main){
                _alertWeather.postValue(alert)
            }
        }
    }
    fun deleteAlert(alert:Alert) {
        viewModelScope.launch {
             iRepo.deleteAlertWeather(alert)
            val alert = iRepo.getAlertWeather()
            withContext(Dispatchers.Main){
                _alertWeather.postValue(alert)
            }
        }
    }
}