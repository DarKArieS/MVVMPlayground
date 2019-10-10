package com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker

interface Heater{
    fun on()
    fun off()
    fun isHot():Boolean
}