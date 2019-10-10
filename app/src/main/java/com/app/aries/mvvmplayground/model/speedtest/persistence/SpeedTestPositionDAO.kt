package com.app.aries.mvvmplayground.model.speedtest.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.aries.mvvmplayground.model.weather.persistence.WeatherData
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface SpeedTestPositionDAO {

    @Query("SELECT * FROM speedtest_pos")
    fun getAll() : Single<List<SpeedTestPosition>>

}