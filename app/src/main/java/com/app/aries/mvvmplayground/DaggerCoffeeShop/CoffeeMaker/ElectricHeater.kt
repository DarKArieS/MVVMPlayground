package com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker

class ElectricHeater : Heater {
    private var heating = false

    override fun isHot(): Boolean {
        return heating
    }

    override fun off() {
        heating = false
    }

    override fun on() {
        println("~ ~ ~ heating ~ ~ ~")
        heating = true
    }
}