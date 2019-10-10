package com.app.aries.mvvmplayground.ReactiveCoffeeShop.CoffeeMaker


import androidx.annotation.MainThread
import io.reactivex.Observable

class Pump {
//    private var afterPumpCallback : ((Coffee)->Unit)? = null

//    fun afterPump(callback : ((Coffee)->Unit)):Pump{
//        afterPumpCallback = callback
//        return this
//    }
//
//    fun pump(water:Water){
//        if (water.degree < 85){
//            println("Degree of water is too low!")
//        }else{
//            Thread{
//                println("=> => pumping => =>")
//                Thread.sleep(1000)
//                val coffee = Coffee()
//                afterPumpCallback?.invoke(coffee)
//            }.start()
//        }
//    }

    fun pump(water:Water):Observable<Coffee>{
        return Observable.create<Coffee> { emitter ->
            if (water.degree < 85){
                emitter.onError(Throwable("Degree of water is too low!"))
            }else{
                println("=> => pumping => =>")
                Thread.sleep(1000)
                val coffee = Coffee()
                emitter.onNext(coffee)
                emitter.onComplete()
            }
        }
    }

}