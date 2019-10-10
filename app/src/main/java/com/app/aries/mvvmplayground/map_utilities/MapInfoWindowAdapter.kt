package com.app.aries.mvvmplayground.map_utilities

import android.view.LayoutInflater
import android.view.View
import com.app.aries.mvvmplayground.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.map_info_windows.view.*

class MapInfoWindowAdapter(private val inflater : LayoutInflater) : GoogleMap.InfoWindowAdapter{
    lateinit var rootView : View
    override fun getInfoContents(marker: Marker): View {
        rootView = inflater.inflate(R.layout.map_info_windows,null, false)

        rootView.infoWindowTitle.text = marker.title
        rootView.infoWindowContent.text = marker.snippet

        return rootView
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}