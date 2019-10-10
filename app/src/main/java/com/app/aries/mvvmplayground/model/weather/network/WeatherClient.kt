package com.app.aries.mvvmplayground.model.weather.network

import com.app.aries.mvvmplayground.model.weather.persistence.WeatherData
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherClient{
    companion object {
        @Volatile
        private var INSTANCE: WeatherClient? = null

        fun getInstance() : WeatherClient =
        INSTANCE ?: synchronized(this) {
            INSTANCE
                ?: WeatherClient().also { INSTANCE = it }
        }
    }

    private val retrofit : Retrofit
    private val service : WeatherAPI
    init{
        val baseUrl = "http://api.openweathermap.org/data/2.5/"

        // interceptor for log
        val httpLog = HttpLoggingInterceptor()
        httpLog.level = HttpLoggingInterceptor.Level.BASIC

        // interceptor for query
        val appIDQuery = QueryParameterInterceptor(
            "appid", "ff287173dfc02d8de3aad212143202e1"
        )
        val unitQuery = QueryParameterInterceptor(
            "units", "imperial"
        )

        val client = OkHttpClient.Builder()
        client.addInterceptor(httpLog)
        client.addInterceptor(appIDQuery)
        client.addInterceptor(unitQuery)
        client.readTimeout(30, TimeUnit.SECONDS)

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .build()

        service = retrofit.create(WeatherAPI::class.java)
    }

    //fun getService() = service

    fun getWeatherData(city:String) : Single<WeatherData> {
        return service.getWeatherResponse(city).map(this::weatherResponseToWeatherData)
    }

    private fun weatherResponseToWeatherData(response:WeatherResponse):WeatherData{
        return WeatherData(
            country = response.name,
            weather = response.weather.first().main,
            temp = response.main.temp,
            tempMax = response.main.temp_max,
            tempMin = response.main.temp_min,
            pressure = response.main.pressure,
            windSpeed = response.wind.speed,
            dt = response.dt
        )
    }
}

