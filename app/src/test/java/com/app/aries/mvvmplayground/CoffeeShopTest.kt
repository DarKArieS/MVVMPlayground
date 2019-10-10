package com.app.aries.mvvmplayground

import com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker.Heater
import com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker.Pump
import com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker.Water
import com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker.CoffeeMaker as ReactiveCoffeeShop
import org.junit.Assert
import org.junit.Test

class CoffeeShopTest {
    @Test
    fun testReactiveCoffeeShop() {
        ReactiveCoffeeShop(
            Water(),
            Heater(),
            Pump()
        ).brew()

        Thread.sleep(5000)
    }
}