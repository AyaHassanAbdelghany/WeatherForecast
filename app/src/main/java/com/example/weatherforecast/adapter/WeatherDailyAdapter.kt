package com.example.weatherforecast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.constant.Constants
import com.example.weatherforecast.databinding.IteamWeatherDayBinding
import com.example.weatherforecast.constant.FormatWeather
import com.example.weatherforecast.pojo.Daily
import com.example.weatherforecast.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import java.util.*

class WeatherDailyAdapter (var lang :String):RecyclerView.Adapter<WeatherDailyAdapter.ViewHolder>() {

    private var days :List<Daily> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setDays(days :List<Daily>){
        this.days = days
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(IteamWeatherDayBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =days.size-1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(days[position+1])

    inner class ViewHolder(private var binding: IteamWeatherDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Daily){

            binding.txtTemp.text = "${
                String.format(
                    Locale(lang),"%d",
                    currentItem.temp.min.toInt())} / ${ String.format(
                Locale(lang),"%d",
                currentItem.temp.max.toInt())} ${
                HomeViewModel.weatherUnits.temp}"
            binding.txtDate.text =FormatWeather.getFormat(currentItem.dt,Constants.formatDay,lang)
            binding.txtStatus.text = currentItem.weather.first().description
            val uri = FormatWeather.getIconFormat(currentItem.weather.first().icon)
            Picasso.get().load(uri).placeholder(R.drawable.clouds)
                .into(binding.imgStatus)
        }
    }
}