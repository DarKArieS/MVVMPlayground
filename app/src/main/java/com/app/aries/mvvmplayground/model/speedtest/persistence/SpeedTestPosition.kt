package com.app.aries.mvvmplayground.model.speedtest.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speedtest_pos")
data class SpeedTestPosition(
    var office : String = "",
    var district : String = "",
    var position : String = "",
    var direction : String = "",
    var latitude : Double,
    var longitude : Double,

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int
)