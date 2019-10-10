package com.app.aries.mvvmplayground.DaggerCoffeeShop.di

import com.app.aries.mvvmplayground.DaggerCoffeeShop.CoffeeMaker.CoffeeMaker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoffeeMakerModule::class])
interface CoffeeComponent {
    fun getCoffeeMaker():CoffeeMaker
}