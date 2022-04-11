package com.example.weatherforecast.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var  navView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        val lang = homeViewModel.getLocalizationSharedPref()
        if (lang.isNotEmpty()) {
            val config = resources.configuration
            val locale = Locale(lang)
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)
           setSupportActionBar(binding.toolbar)

        navView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navView.setupWithNavController(navController)

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_settings,
                    R.id.navigation_favourite,
                    R.id.navigation_alert
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }

    override fun onResume() {
        super.onResume()
        val lang = homeViewModel.getLocalizationSharedPref()
        if (lang.isNotEmpty()) {
            val config = resources.configuration
            val locale = Locale(lang)
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
    private fun init() {
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource.getInstance(this),
                SharedPrefs.getInstance(this),
                this
            ),this
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onBackPressed() {
        if(navView.selectedItemId==R.id.navigation_home)
            finishAffinity()
        else
        super.onBackPressed()

    }
   }
