package com.example.weatherforecast.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.LocationViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import com.example.weatherforecast.viewmodel.viewmodelfactory.LocationViewModelFactory
import java.util.*

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

        val lang = homeViewModel.getLocalizationSharedPref()
        val config = resources.configuration
        val locale = Locale(lang)
        config.setLocale(locale)
        resources.updateConfiguration(config,resources.displayMetrics)

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
            locationViewModel.addGpsOrMapSharedPre(radioButton.text.toString())

            val  radioGroupLang = view.findViewById<RadioGroup>(R.id.radioGroupLang)
            val radioButtonLang :RadioButton= view.findViewById(radioGroupLang!!.checkedRadioButtonId)
            when(radioButtonLang.text){
                Units.Arabic.name -> homeViewModel.addLocalizationSharedPref(Units.Arabic.unit)
                Units.English.name -> homeViewModel.addLocalizationSharedPref(Units.English.unit)
            }

            var unit = Units.Standard
            val  radioGroupTemp = view.findViewById<RadioGroup>(R.id.radioGroupTemp)
            val radioButtonTemp :RadioButton= view.findViewById(radioGroupTemp!!.checkedRadioButtonId)
            when(radioButtonTemp.text){
                Units.Kelvin.name->unit  = Units.Standard
                Units.Celsius.name->unit = Units.Metric
                Units.Fahrenheit.name->unit= Units.Imperial
            }
            homeViewModel.addUnitSharedPrefs(unit)
            if(radioButton.text=="GPS")
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
            Units.Kelvin.name ->{binding.radioMeter.isChecked=true}
            Units.Celsius.name->binding.radioMeter.isChecked= true
            Units.Fahrenheit.name->binding.radioMile.isChecked=true
        }
    }
    private fun selectRadioTemp(selectOption: String){
        when(selectOption){
            Units.MeterPerSec.unit->binding.radioKelvin.isChecked=true
            Units.MilesPerHour.unit->binding.radioFahrenit.isChecked= true
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
                , SharedPrefs.getInstance(requireContext()),requireContext()))
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

    }

}