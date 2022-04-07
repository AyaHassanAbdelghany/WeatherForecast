package com.example.weatherforecast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.IteamWeatherFavouriteBinding
import com.example.weatherforecast.pojo.BaseWeather


class FavouriteAdapter ( listner: OnClickFavouriteWeatherListner):RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private var weatherList :List<BaseWeather> = listOf()
    private  var listner :OnClickFavouriteWeatherListner = listner

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
            binding.txtCountry.text = currentItem.timezone
            binding.constraint.setOnClickListener{
                listner.onClick(currentItem)
            }
        }

    }
}