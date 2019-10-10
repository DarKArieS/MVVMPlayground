package com.app.aries.mvvmplayground.DaggerActivity.di

import com.app.aries.mvvmplayground.DaggerActivity.DaggerredMainActivity
import dagger.Component
import dagger.android.AndroidInjectionModule


@Component
    (modules = [
    MainActivityModule::class,
//    MainActivityBuilderModule::class,
    AndroidInjectionModule::class
])
interface MainActivityComponent{
    //    @Component.Builder
    //    interface Builder {
    //        @BindsInstance
    //        fun activity(activity: DaggerMainActivity): Builder
    //
    //        fun build(): MainActivityComponent
    //    }

    fun inject(activity: DaggerredMainActivity)
}