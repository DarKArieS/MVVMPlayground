package com.app.aries.mvvmplayground.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.aries.mvvmplayground.CustomApplication
import com.app.aries.mvvmplayground.model.weather.WeatherRepository
import com.app.aries.mvvmplayground.model.weather.persistence.DataStatus
import com.app.aries.mvvmplayground.model.weather.persistence.WeatherData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FirstViewModel : ViewModel(){
    private var disposables = CompositeDisposable()
    private val weatherRepository = WeatherRepository(CustomApplication.getInstance())

    var weatherData = MutableLiveData<WeatherData>()
    var listData = MutableLiveData<MutableList<String>>(mutableListOf("hi"))

    fun getWeather(){
        Timber.tag("FirstViewModel").d("getWeather")
        disposables.add(
            weatherRepository.getWeatherByCity("Tainan")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(weatherData::postValue){
                    //onError
                    Timber.tag("FirstViewModel").d("no data comes from Repo: $it")
                    weatherData.postValue(
                        weatherData.value.apply { this?.dataResource = DataStatus.NetworkError }
                    )
                }
        )
    }

    fun modifyListData(){
        listData.value?.add("hello~")
        Timber.tag("FirstViewModel").d(listData.value.toString())
        listData.notifyObserver()
    }

    fun cancelRequests(){
        disposables.clear()
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}