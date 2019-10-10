package com.app.aries.mvvmplayground.map_utilities

import com.google.maps.android.clustering.ClusterItem
import com.google.android.gms.maps.model.LatLng

class MarkerItem : ClusterItem {

    private var mPosition: LatLng
    private var mTitle: String = ""
    private var mSnippet: String = ""

    constructor(latLng:LatLng){
        mPosition = latLng
    }

    constructor(lat: Double, lng: Double){
        mPosition = LatLng(lat, lng)
    }

    constructor(lat: Double, lng: Double, title: String, snippet: String){
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    fun title(title:String):MarkerItem{
        mTitle = title
        return this
    }

    fun snippet(snippet:String):MarkerItem{
        mSnippet = snippet
        return this
    }

}