package com.example.weatherforecast.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivitySplashBinding
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.viewmodel.LocationViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.LocationViewModelFactory
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationViewModelFactory: LocationViewModelFactory
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        homeViewModel.addLocalizationSharedPref(homeViewModel.getLocalizationDevice())

         if(locationViewModel.getGpsOrMapSharedPre().isEmpty())
         showDialog()
         else
            startActivity(Intent(applicationContext, MainActivity::class.java))
    }

    private fun  init(){

        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(WeatherClient.getInstance()
            ,ConcreteLocalSource.getInstance(this)
            , SharedPrefs.getInstance(this),this))
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    locationViewModelFactory = LocationViewModelFactory(
        WeatherRepo.getInstance(
            WeatherClient.getInstance()
            ,ConcreteLocalSource.getInstance(this)
            , SharedPrefs.getInstance(this),this),this)
    locationViewModel = ViewModelProvider(this, locationViewModelFactory)[LocationViewModel::class.java]

}

    private fun showDialog(){
        var radioButton :RadioButton
        val builder = AlertDialog.Builder(this)
            .create()
        val view = layoutInflater.inflate(R.layout.dialog,null)
        val  btnSubmit= view.findViewById<Button>(R.id.btnSubmit)
        val  radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupLocation)
        builder.setView(view)

        btnSubmit.setOnClickListener {
            val selectedOption: Int = radioGroup!!.checkedRadioButtonId
            radioButton = view.findViewById(selectedOption)
            locationViewModel.addGpsOrMapSharedPre(radioButton.text.toString())
            if(radioButton.text == "Map")
                startActivity(Intent(applicationContext, MapsActivity::class.java))
            else startActivity(Intent(applicationContext, MainActivity::class.java))
            builder.dismiss()
            finish()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}