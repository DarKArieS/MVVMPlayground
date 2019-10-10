package com.app.aries.mvvmplayground.model.weather.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "weatherTable")
data class WeatherData(
        var country : String = "",
        var weather : String = "",
        var temp: Float = 0f, // degree F ...
        var tempMax : Float = 0f,
        var tempMin : Float = 0f,
        var pressure : Float = 0f,
        var windSpeed : Float = 0f,
        var dt : Long = 0,

        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = UUID.randomUUID().toString(),

        @Ignore
        var dataResource: DataStatus = DataStatus.NoData
    )

enum class DataStatus{
        NoData,
        FromCache,
        FromNetwork,
        NetworkError
}