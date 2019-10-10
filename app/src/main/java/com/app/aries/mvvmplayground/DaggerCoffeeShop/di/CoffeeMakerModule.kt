package com.app.aries.mvvmplayground.DaggerCoffeeShop.di

import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.ElectricHeater
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.Heater
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.Pump
import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.Thermosiphon
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [PumpModule::class])
class CoffeeMakerModule {
    @Provides
    fun provideHeater():Heater{
        return ElectricHeater()
    }

//    @Provides
//    fun providePump(pump: Thermosiphon): Pump {
//
//        return pump
//    }
}