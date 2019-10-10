package com.app.aries.mvvmplayground.DaggerActivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DaggerredViewModelFactory(msg:String): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when{
            (modelClass.isAssignableFrom(DaggerredMainViewModel::class.java))-> DaggerredMainViewModel() as T
            else-> throw IllegalArgumentException("unknown model class $modelClass")
        }
    }
}