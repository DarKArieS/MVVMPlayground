package com.app.aries.mvvmplayground.DaggerCoffeeShop

import com.app.aries.mvvmplayground.DaggerCoffeeShop.di.CoffeeComponent
import com.app.aries.mvvmplayground.DaggerCoffeeShop.di.DaggerCoffeeComponent

class CoffeeShop {
    companion object{
        fun getCoffee(){
            val coffeeProvider = DaggerCoffeeComponent.builder().build()
            coffeeProvider.getCoffeeMaker().brew()
        }
    }
}