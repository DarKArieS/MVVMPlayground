package com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker

import javax.inject.Inject

class CoffeeMaker@Inject constructor(private val heater: Heater, private val pump: Pump) {
    fun brew(){
        heater.on()
        pump.pump()
        println(" [_]P coffee! [_]P ")
        heater.off()
    }
}

//class Coffeemaker(injector:Injector)
//{
//    private val heater = injector.provideHeater()
//    private val pump = injector.providePump()
//
//    fun brew(){
//        heater.on()
//        pump.pump()
//        println(" [_]P coffee! [_]P ")
//        heater.off()
//    }
//}