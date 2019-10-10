package com.app.aries.mvvmplayground.model.weather.persistence

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single


@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather : WeatherData) : Maybe<Long>

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    fun updateWeatherByID(weather : WeatherData) : Maybe<Long>

    @Query("SELECT * FROM weatherTable")
    fun getAll() : Single<List<WeatherData>>

    @Query("SELECT * FROM weatherTable WHERE country = :country")
    fun getWeatherByCity(country : String) : Single<List<WeatherData>>

    @Query("DELETE FROM weatherTable")
    fun deleteAll()

}