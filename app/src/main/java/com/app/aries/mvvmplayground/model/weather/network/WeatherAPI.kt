package com.app.aries.mvvmplayground.model.weather.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI{
    @GET("weather")
    fun getWeatherResponse(@Query("q") city: String): Single<WeatherResponse>
}