package com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker

class Water : Heatable{
    var degree = 25

    override fun beHeated(energy : Int){
        degree += energy
    }
}

