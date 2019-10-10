package com.app.aries.mvvmplayground.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.aries.mvvmplayground.viewmodel.map.MapViewModel


class ViewModelFactory: ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when{
            (modelClass.isAssignableFrom(FirstViewModel::class.java))-> FirstViewModel() as T
            (modelClass.isAssignableFrom(MapViewModel::class.java))-> MapViewModel() as T
            else-> throw IllegalArgumentException("unknown model class $modelClass")
        }
    }
}