package com.example.weatherforecast.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.IteamWeatherFavouriteBinding
import com.example.weatherforecast.pojo.Alert


class AlertAdapter ( listner:OnClickAlertListner):RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    private  var alert :List<Alert> = listOf()
    private  var listner :OnClickAlertListner = listner


    @SuppressLint("NotifyDataSetChanged")
    fun setWeather(alert :List<Alert>){
        this.alert = alert
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(IteamWeatherFavouriteBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int =alert.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(alert[position])


    inner class ViewHolder(private var binding: IteamWeatherFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: Alert){
            binding.txtCountry.text = "${currentItem.name}${" "}${adapterPosition+1}"
            binding.imgDelete.setOnClickListener{
                Log.i("TAG", "onClick:$adapterPosition ")

                listner.onClick(currentItem, adapterPosition)
            }
        }

    }
}