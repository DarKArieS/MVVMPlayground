package com.app.aries.mvvmplayground.map_utilities

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable

class RxMapView{
    companion object{
        fun animateCamera(mMap: GoogleMap, cameraUpdate : CameraUpdate): Observable<Unit> {
            return Observable.create<Unit>{
                val callback = object: GoogleMap.CancelableCallback{
                    override fun onCancel() {
                        it.onComplete()
                    }

                    override fun onFinish() {
                        it.onNext(Unit)
                        it.onComplete()
                    }
                }
                mMap.animateCamera(cameraUpdate,callback)
            }
        }
    }
}
