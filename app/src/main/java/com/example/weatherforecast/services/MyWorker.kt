package com.example.weatherforecast.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecast.R
import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.view.activity.MainActivity
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class MyWorker(var context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private var repo = WeatherRepo.getInstance(WeatherClient.getInstance(),
        ConcreteLocalSource.getInstance(context),SharedPrefs.getInstance(context),context)
    private val CHANNEL_ID:String="2"

    override suspend fun doWork(): Result {
        val gson = Gson()
        var location :String = SharedPrefs.getInstance(context).getLocationSharedPre()
        val latLong: LatLng = gson.fromJson(location, LatLng::class.java)
        var unit :String = SharedPrefs.getInstance(context).getUnitSharedPre()
        val units: Units = gson.fromJson(unit, Units::class.java)

        val weather = repo.getWeatherOnline(latLong.latitude,latLong.longitude,units,
            SharedPrefs.getInstance(context).getLocalizationSharedPref())
        if(weather.alert.isNullOrEmpty()){
            var message = "The Weather is Stable"
            showNotification(message)
        }
        else{
            var message = {"The Weather is will be ${weather.alert!!.first().event}"}
            showNotification(message.toString())
        }
        return Result.success()
        }
    private fun showNotification(message :String){

        val notifyIntent = Intent(applicationContext, MainActivity::class.java)
        notifyIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                val name: CharSequence = "channel"
                val desc = "desc"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = desc
                channel.setSound(
                    Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/alertweather"),
                    audioAttributes
                )
                val notificationManager = applicationContext.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }
//        val sound =
//            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/quite_impressed.mp3")
            //val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notification: Notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.weather)
                .setContentTitle("alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                 .setAutoCancel(true).build()
            val managerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            managerCompat.notify(2, notification)
        }
}