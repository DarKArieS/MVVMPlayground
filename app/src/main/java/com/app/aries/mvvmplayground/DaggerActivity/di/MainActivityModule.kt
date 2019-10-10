package com.app.aries.mvvmplayground.DaggerActivity.di

import com.app.aries.mvvmplayground.DaggerActivity.DaggerredMainActivity
import com.app.aries.mvvmplayground.DaggerActivity.viewmodel.DaggerredViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Qualifier



@Module
class MainActivityModule(val msg:String) {
    @Provides
    @DaggerredMainViewModelFactory
    fun provideViewModelFactory() : DaggerredViewModelFactory {
        return DaggerredViewModelFactory(msg)
    }
}

@Module
abstract class MainActivityBuilderModule{
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): DaggerredMainActivity
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DaggerredMainViewModelFactory