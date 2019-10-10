package com.app.aries.mvvmplayground

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import timber.log.Timber

class CustomApplication : Application() {
    companion object {
        private var INSTANCE : CustomApplication? = null

        fun getInstance() : CustomApplication = INSTANCE!!
    }

    override fun onCreate(){
        super.onCreate()
        INSTANCE = this
        Timber.plant(Timber.DebugTree())
        Logger.addLogAdapter(AndroidLogAdapter())
        Timber.d("application onCreate")
    }
}