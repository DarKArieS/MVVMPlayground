package com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker

class Injector{
    private val heater = ElectricHeater()
    private val pump = Thermosiphon(heater)

    fun provideHeater() : Heater{
        return heater
    }

    fun providePump() : Pump{
        return pump
    }
}