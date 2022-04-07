package com.example.weatherforecast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.constant.Constants
import com.example.weatherforecast.databinding.ItemWeatherHourBinding
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.pojo.Hourly
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso



class WeatherHourlyAdapter :RecyclerView.Adapter<WeatherHourlyAdapter.ViewHolder>()  {

    private var hours :List<Hourly> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setHours(hours :List<Hourly>){
        this.hours = hours
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(
        ItemWeatherHourBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =hours.size/2
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(hours[position])

    inner class ViewHolder(private var binding: ItemWeatherHourBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(currentItem: Hourly){

            binding.txtTemp.text = currentItem.temp.toString()+ HomeViewModel.weatherUnits.temp.unit
            binding.txtTime.text =FormatWeather.getFormat(currentItem.dt,Constants.formatTime)
            val uri = FormatWeather.getIconFormat(currentItem.weather.first().icon)
            Picasso.get().load(uri).placeholder(R.drawable.clouds)
                .into(binding.imgStatus)
        }
    }
}