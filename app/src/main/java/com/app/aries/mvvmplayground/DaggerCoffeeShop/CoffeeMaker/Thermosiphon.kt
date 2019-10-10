package com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker

import javax.inject.Inject

class Thermosiphon @Inject constructor(val heater: Heater) : Pump {
    override fun pump() {
        if(heater.isHot()){
            println("=> => pumping => =>")
        }
    }
}

