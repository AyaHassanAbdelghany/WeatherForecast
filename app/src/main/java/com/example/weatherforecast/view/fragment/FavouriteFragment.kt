package com.example.weatherforecast.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.adapter.FavouriteAdapter
import com.example.weatherforecast.adapter.OnClickFavWeatherListner
import com.example.weatherforecast.databinding.FragmentFavouriteBinding
import com.example.weatherforecast.viewmodel.FavWeatherViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.FavWeatherViewModelFactory
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.view.activity.FavouriteDetailsActivity
import com.example.weatherforecast.view.activity.MapsActivity
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.google.gson.Gson
import java.util.*


class FavouriteFragment : Fragment(),OnClickFavWeatherListner {

   private lateinit var binding:FragmentFavouriteBinding
   private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favWeatherViewModel: FavWeatherViewModel
    private lateinit var favWeatherViewModelFactory: FavWeatherViewModelFactory
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lang = homeViewModel.getLocalizationSharedPref()
        val config = resources.configuration
        val locale = Locale(lang)
        config.setLocale(locale)
        resources.updateConfiguration(config,resources.displayMetrics)

        favWeatherViewModel.favWeather.observe(viewLifecycleOwner){
            favouriteAdapter.setWeather(it)
            binding.recyclerFav.adapter=favouriteAdapter
        }
        binding.fabFav.setOnClickListener {
            startActivity(Intent(activity, MapsActivity::class.java).putExtra("Fav",true))
        }
    }

    override fun onResume() {
        super.onResume()
        favWeatherViewModel.getFavWeather()
    }
    private fun init(){
        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(WeatherClient.getInstance()
            ,ConcreteLocalSource.getInstance(requireContext())
            , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        favWeatherViewModelFactory = FavWeatherViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(requireContext())
                , SharedPrefs.getInstance(requireContext()),requireContext()))
        favWeatherViewModel = ViewModelProvider(this, favWeatherViewModelFactory)[FavWeatherViewModel::class.java]
        favouriteAdapter = FavouriteAdapter(homeViewModel,this)
        binding.recyclerFav.adapter = favouriteAdapter
    }

    override fun onClickDelete(baseWeather: BaseWeather) {
        favWeatherViewModel.deleteFavWeather(baseWeather)
    }

    override fun onClickDetails(baseWeather: BaseWeather) {
        val gson = Gson()
        val json = gson.toJson(baseWeather)
        startActivity(Intent(activity, FavouriteDetailsActivity::class.java)
            .putExtra("object",json))
    }
}