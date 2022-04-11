package com.example.weatherforecast.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.repo.RepoInterface
import com.example.weatherforecast.viewmodel.HomeViewModel

class HomeViewModelFactory(private val iRep :RepoInterface,private  val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(iRep!!,context) as T
        } else {
            throw IllegalArgumentException("Error")
        }    }
}