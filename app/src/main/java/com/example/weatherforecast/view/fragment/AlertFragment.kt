package com.example.weatherforecast.view.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapter.AlertAdapter
import com.example.weatherforecast.adapter.OnClickAlertListner
import com.example.weatherforecast.databinding.FragmentAlertBinding
import com.example.weatherforecast.localsource.db.ConcreteLocalSource
import com.example.weatherforecast.localsource.shared.SharedPrefs
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.pojo.Alert
import com.example.weatherforecast.repo.WeatherRepo
import com.example.weatherforecast.viewmodel.AlertViewModel
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.example.weatherforecast.viewmodel.viewmodelfactory.AlertViewModelFactory
import com.example.weatherforecast.viewmodel.viewmodelfactory.HomeViewModelFactory
import java.util.*


class AlertFragment : Fragment() ,OnClickAlertListner {

    private lateinit var binding:FragmentAlertBinding
    private val myCalendar = Calendar.getInstance()
    private lateinit var alertViewModel :AlertViewModel
    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var homeViewModel : HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var time:String
    private  var duration:Int = 0
    private var alert :List<Alert> = listOf( Alert())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAlertBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertViewModel.alertWeather.observe(viewLifecycleOwner){
            alertAdapter.setWeather(it)
            binding.recyclerAlert.adapter = alertAdapter
        }
        binding.fabAlert.setOnClickListener{
            showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        alertViewModel.getAlertWeather()
    }
    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
            .create()
        val view = layoutInflater.inflate(R.layout.alert,null)
        val  btnSubmit= view.findViewById<Button>(R.id.btnSubmit)
        val txtDateEnd = view.findViewById<TextView>(R.id.txtDateEnd)
        val txtDateFrom = view.findViewById<TextView>(R.id.txtDateFrom)
        val txtTime = view.findViewById<TextView>(R.id.txtTime)
        val txtHour = view.findViewById<TextView>(R.id.txtHour)


        val thisYear = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val hour = myCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = myCalendar.get(Calendar.MINUTE)
        time= "$hour:$minute"

        builder.setView(view)

        txtDateFrom.text = "$day-${month+1}-$thisYear"
        txtDateEnd.text = "$day-${month+1}-$thisYear"
        txtHour.text = time

        txtDateEnd.setOnClickListener{
              val datePicker = DatePickerDialog(requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                txtDateEnd.text = "$dayOfMonth-${monthOfYear+1}-$year"
                duration = dayOfMonth-day
            },thisYear,month,day)
            datePicker.show()
        }
        txtTime.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(),{
                    _, selectHour, selectMinute ->
               time ="$selectHour:$selectMinute"
                txtHour.text = "$selectHour:$selectMinute"

            }  ,hour,minute,true)
            timePicker.show()
        }


        btnSubmit.setOnClickListener{
            builder.cancel()
            alertAdapter.setWeather(alert)
            binding.recyclerAlert.adapter = alertAdapter
            alertViewModel.insertAlert(Alert())
            alertViewModel.getAlertWeather()
            setAlerts(alertViewModel.setDaysAlert(txtDateFrom.text.toString(),duration))
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }
    private fun setAlerts(alerts:List<String>){
            alertViewModel.createListRequests(alerts,time)
    }

    private fun init(){
        alertAdapter = AlertAdapter(this)
        binding.recyclerAlert.adapter = alertAdapter

        alertViewModelFactory = AlertViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
                , ConcreteLocalSource.getInstance(requireContext())
                , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        alertViewModel = ViewModelProvider(this,alertViewModelFactory)[AlertViewModel::class.java]
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(
                WeatherClient.getInstance()
            , ConcreteLocalSource.getInstance(requireContext())
            , SharedPrefs.getInstance(requireContext()),requireContext()),requireContext())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onClick(alert: Alert,position:Int) {
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag((position).toString())
        alertViewModel.deleteAlert(alert)
    }
}