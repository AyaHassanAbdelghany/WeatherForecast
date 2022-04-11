package com.example.weatherforecast.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.repo.RepoInterface
import com.example.weatherforecast.viewmodel.AlertViewModel

class AlertViewModelFactory (private  val iRepo:RepoInterface,private val context : Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(iRepo,context) as T
        } else {
            throw IllegalArgumentException("Error")
        }    }
}