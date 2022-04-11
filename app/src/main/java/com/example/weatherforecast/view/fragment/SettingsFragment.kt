package com.example.weatherforecast.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import com.example.weatherforecast.enum.Units
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.view.activity.MainActivity
import com.example.weatherforecast.view.activity.MapsActivity
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.LocationViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.example.weatherforecast.viewmodel.viewmodelfactory.LocationViewModelFactory

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationViewModelFactory: LocationViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedOption :Int

        selectRadioTempWind( homeViewModel.getUnitSharedPrefs())

        if(locationViewModel.getGpsOrMapSharedPre()=="GPS"){
            binding.radioGps.isChecked = true
        }
        else{
            binding.radioMap.isChecked = true
        }
        if(homeViewModel.getLocalizationSharedPref()=="en"){
            binding.radioEng.isChecked= true
        }
        else{
            binding.radioArab.isChecked = true
        }


        binding.radioGroupTemp.setOnCheckedChangeListener { radioGroup, i ->
            selectedOption = radioGroup.checkedRadioButtonId
            selectRadioWind(view.findViewById<RadioButton?>(selectedOption).text.toString())
        }

        binding.radioGroupWindSpeed.setOnCheckedChangeListener { radioGroup, i ->
            selectedOption = radioGroup.checkedRadioButtonId
            selectRadioTemp(view.findViewById<RadioButton?>(selectedOption).text.toString())
        }

        binding.btnSubmit.setOnClickListener {

            val  radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupLocation)
            val radioButton :RadioButton= view.findViewById(radioGroup!!.checkedRadioButtonId)

            when (radioButton.text){
                getString(R.string.gps)-> locationViewModel.addGpsOrMapSharedPre("GPS")
                getString(R.string.map)-> locationViewModel.addGpsOrMapSharedPre("Map")
            }

            val  radioGroupLang = view.findViewById<RadioGroup>(R.id.radioGroupLang)
            val radioButtonLang :RadioButton= view.findViewById(radioGroupLang!!.checkedRadioButtonId)
            when(radioButtonLang.text){
                getString(R.string.arabic) -> homeViewModel.addLocalizationSharedPref(Units.Arabic.unit)
                getString(R.string.english) -> homeViewModel.addLocalizationSharedPref(Units.English.unit)
            }

            var unit = Units.Standard
            val  radioGroupTemp = view.findViewById<RadioGroup>(R.id.radioGroupTemp)
            val radioButtonTemp :RadioButton= view.findViewById(radioGroupTemp!!.checkedRadioButtonId)

            when(radioButtonTemp.text){
                getString(R.string.temp_kelvin_radioBtn)->unit = Units.Standard
                getString(R.string.temp_celsius_radioBtn)->unit = Units.Metric
                getString(R.string.temp_fahrenheit_radioBtn)->unit= Units.Imperial
            }

            homeViewModel.addUnitSharedPrefs(unit)
            if(radioButton.text==getString(R.string.gps))
                startActivity(Intent(requireContext(), MainActivity::class.java))
           else  startActivity(Intent(requireContext(), MapsActivity::class.java))
        }
    }



    private fun selectRadioTempWind(unit :Units){
        when(unit.name){
            Units.Standard.name ->{
                binding.radioMeter.isChecked=true
                binding.radioKelvin.isChecked=true
            }
            Units.Metric.name->{
                binding.radioMeter.isChecked= true
                binding.radioCelsuis.isChecked=true
            }
            Units.Imperial.name->{
                binding.radioMile.isChecked=true
                binding.radioFahrenit.isChecked=true
            }
        }
    }
    private fun selectRadioWind(selectOption:String){

        when(selectOption){
            getString(R.string.temp_kelvin_radioBtn) ->{binding.radioMeter.isChecked=true}
            getString(R.string.temp_celsius_radioBtn) ->binding.radioMeter.isChecked= true
            getString(R.string.temp_fahrenheit_radioBtn) ->binding.radioMile.isChecked=true
        }
    }
    private fun selectRadioTemp(selectOption: String){
        when(selectOption){
            getString(R.string.meter_radioBtn) ->binding.radioKelvin.isChecked=true
            getString(R.string.mile_radioBtn) ->binding.radioFahrenit.isChecked= true
        }
    }

    private fun  init(){
        locationViewModelFactory = LocationViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(requireContext())
                , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        locationViewModel = ViewModelProvider(this, locationViewModelFactory)[LocationViewModel::class.java]


        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(requireContext())
                , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

    }

}