package com.app.aries.mvvmplayground.model.weather

import android.content.Context
import com.app.aries.mvvmplayground.model.weather.network.WeatherClient
import com.app.aries.mvvmplayground.model.weather.persistence.DataStatus
import com.app.aries.mvvmplayground.model.weather.persistence.WeatherData
import com.app.aries.mvvmplayground.model.weather.persistence.WeatherDatabase
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WeatherRepository(context: Context) {
//    companion object {
//        @Volatile
//        private var INSTANCE: WeatherRepository? = null
//
//        fun getInstance(context:Context) : WeatherRepository =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE
//                    ?: WeatherRepository(context.applicationContext).also { INSTANCE = it }
//            }
//    }

    private val networkClient =  WeatherClient.getInstance()
    private val databaseService = WeatherDatabase.getInstance(context).weatherDao()

    fun getWeatherByCity(city : String) : Flowable<WeatherData>
    {
        Timber.tag("Repository").d("prepare flowable...")
        val fromNetwork = networkClient.getWeatherData(city)
            .map(this::saveWeatherToDataBase)
            .delay(2000, TimeUnit.MILLISECONDS)
        val fromCache = databaseService.getWeatherByCity(city)
            .map(this::getLatestWeatherFromDataBase)


        val fromCatchThanSave = fromCache.flatMap {
            // concatMap : return with original ordering!
            databaseService.insertWeather(it).toSingle()
        }

        return Single.concat(fromCache,fromNetwork)
    }

    private fun saveWeatherToDataBase(newWeather : WeatherData) : WeatherData{
        Timber.tag("Repository").d("save info from network to database")

        //ToDo use old id to update database!
        if(""!=oldID){
            newWeather.id = oldID
        }
        val disposable = databaseService
            .insertWeather(newWeather)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Timber.tag("Repository").d("save weather from network! $it")
            },{
                Timber.tag("Repository").d("save weather failed! $it")
            })
        newWeather.dataResource = DataStatus.FromNetwork
        return newWeather
    }

    private var oldID =""

    private fun getLatestWeatherFromDataBase(list : List<WeatherData>) : WeatherData{
        Timber.tag("Repository").d("emit info from database $list")
        return if(list.isEmpty()){
            Timber.tag("Repository").d("no cache from database")
            WeatherData(id="none", dataResource = DataStatus.NoData)
        }else{
            list.last().dataResource = DataStatus.FromCache
            oldID = list.last().id
            list.last()
        }
    }

}