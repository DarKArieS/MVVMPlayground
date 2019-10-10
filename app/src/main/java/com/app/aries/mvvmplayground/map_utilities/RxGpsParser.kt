package com.app.aries.mvvmplayground.map_utilities

import android.content.Context
import io.reactivex.Observable
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import java.io.IOException


class RxGpsParser{
    companion object{
        fun parse(input : String, context: Context) = Observable.create<Gpx>{
            val gpxParser = GPXParser()
            val inputStream = context.assets.open(input)

            lateinit var parsed : Gpx
            try {
                parsed = gpxParser.parse(inputStream)
                it.onNext(parsed)
                it.onComplete()
            } catch (e: IOException) {
                //it.onError(Exception("GGWP: $e"))
            }
        }
    }
}