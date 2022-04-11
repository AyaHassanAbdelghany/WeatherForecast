package com.example.weatherforecast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.IteamWeatherFavouriteBinding
import com.example.weatherforecast.pojo.BaseWeather
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.LatLng


class FavouriteAdapter (var homeViewMoedl :HomeViewModel,listner: OnClickFavWeatherListner):RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private var weatherList :List<BaseWeather> = listOf()
    private  var listner :OnClickFavWeatherListner = listner

    @SuppressLint("NotifyDataSetChanged")
    fun setWeather(weatherList :List<BaseWeather>){
        this.weatherList = weatherList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(IteamWeatherFavouriteBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =weatherList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(weatherList[position])


    inner class ViewHolder(private var binding: IteamWeatherFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: BaseWeather){
            binding.txtCountry.text = homeViewMoedl.getCity(LatLng(currentItem.lat,currentItem.lon))
            binding.imgDelete.setOnClickListener{
                listner.onClickDelete(currentItem)
            }
            binding.constraint.setOnClickListener {
                listner.onClickDetails(currentItem)
            }
        }
    }
}