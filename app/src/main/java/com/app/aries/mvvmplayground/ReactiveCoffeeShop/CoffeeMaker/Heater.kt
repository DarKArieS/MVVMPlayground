package com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker

import io.reactivex.Observable

class Heater{
    private var energy = 0
    private var afterHeatedCallback : ((Int)->Unit)? = null

//    fun afterHeated(callback : ((Int)->Unit)):Heater{
//        afterHeatedCallback = callback
//        return this
//    }
//
//    fun on(){
//        Thread{
//            println("~ ~ ~ heating ~ ~ ~")
//            Thread.sleep(1000)
//            energy = 60
//            afterHeatedCallback?.invoke(energy)
//        }.start()
//    }
//
//    fun off(){
//        energy = 0
//        afterHeatedCallback?.invoke(-50)
//    }

    fun on(): Observable<Int> {
        return Observable.create<Int> {emitter->
            println("~ ~ ~ heating ~ ~ ~")
            Thread.sleep(1000)
            energy = 60
            emitter.onNext(energy)
            emitter.onComplete()
        }
    }

    fun off(){
        energy = 0
    }

//    fun off(): Observable<Int>{
//        return Observable.create<Int>{emitter->
//            Thread.sleep(1000)
//            energy = 0
//            emitter.onNext(energy)
//            emitter.onComplete()
//        }
//    }

    fun getEnergy() = energy
}