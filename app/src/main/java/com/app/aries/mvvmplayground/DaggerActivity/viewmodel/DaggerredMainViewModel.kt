package com.app.aries.mvvmplayground.DaggerActivity.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DaggerredMainViewModel : ViewModel(){

    var helloString = MutableLiveData<String>()

    fun updateMessage(){
        if(null == helloString.value){
            Log.d("livedata","helloString is null!")
            Log.d("livedata","helloString updated!")
            //helloString.postValue("oh yeah!")
            helloString.postValue("oh yeah!")
        }else{
            Log.d("livedata","helloString is not null: ${helloString.value}!")
        }
    }

}