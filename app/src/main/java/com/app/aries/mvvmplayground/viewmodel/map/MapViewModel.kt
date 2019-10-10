package com.app.aries.mvvmplayground.viewmodel.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.aries.mvvmplayground.model.speedtest.persistence.SpeedTestPosition
import com.app.aries.mvvmplayground.model.speedtest.persistence.SpeedTestPositionDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MapViewModel : ViewModel(){
    private var disposables = CompositeDisposable()
    private var database = SpeedTestPositionDatabase.getInstance()

    var stpData = MutableLiveData<List<SpeedTestPosition>>()

    fun getSpeedTestPositionData(){
        disposables.add(
            database.speedTestPositionDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    Timber.tag("MapViewModel").d("getSpeedTestPositionData")
                }
                .subscribe(stpData::postValue){
                    //ToDo onError
                }
        )
    }

}