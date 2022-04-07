package com.example.weatherforecast.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.repo.RepoInterface
import com.example.weatherforecast.viewmodel.LocationViewModel
import java.lang.IllegalArgumentException

class LocationViewModelFactory(var iRepo:RepoInterface,var context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationViewModel::class.java)){
            LocationViewModel(iRepo,context) as T
        }
        else{
            throw IllegalArgumentException("Error")
        }
    }
}