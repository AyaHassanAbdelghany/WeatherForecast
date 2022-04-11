package com.example.weatherforecast.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weatherAlert" )

data class Alert(

    @PrimaryKey (autoGenerate = true)
    @SerializedName("idAlert")
    var idAlert:Int ,
    @SerializedName("name")
    var name:String ="Alert"
) {
    constructor() : this(idAlert = 0, name= "Alert")
}
