package com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class CoffeeMaker(
    private val water: Water,
    private val heater: Heater,
    private val pump: Pump
){
    private val disposable = CompositeDisposable()

    fun brew(){

//        pump.afterPump{ coffee->
//            println(" [_]P coffee! [_]P ")
//            heater.off()
//        }
//
//        heater.afterHeated { energy->
//            water.beHeated(energy)
//            if (energy > 0)
//                pump.pump(water)
//        }.on()

        val procedure = heater.on().flatMap {energy->
            water.beHeated(energy)
            pump.pump(water)

        }

//        .flatMap {coffee->
//            println(" [_]P coffee! [_]P ")
//            heater.off()
//        }

        disposable.add(
            procedure.subscribe {energy->
//                water.beHeated(energy)
                println(" [_]P coffee! [_]P ")
                heater.off()
            }
        )
    }

    fun cancel(){
        disposable.clear()
    }
}