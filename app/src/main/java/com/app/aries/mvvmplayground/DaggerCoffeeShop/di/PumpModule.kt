package com.app.aries.mvvmplayground.DaggerCoffeeShop.di

import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.ElectricHeater
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.Heater
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.Pump
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.Thermosiphon
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class PumpModule{
    @Binds
    abstract fun providePump(pump: Thermosiphon):Pump
}

//@Module
//class PumpModule {
//    @Provides
//    @Singleton
//    fun provideHeater():Heater{
//        return ElectricHeater()
//    }
//}
