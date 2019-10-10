package com.app.aries.mvvmplayground.map_utilities

import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.app.aries.mvvmplayground.ColorScaleLegendView.ColorScaleLegendView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import timber.log.Timber

class ColoredPathManager(
    val context: Context,
    private val map : GoogleMap,
    private val maxNumPath : Int = 300
) : GoogleMap.OnPolylineClickListener
{
    val trackPoints = mutableListOf<TrackPoint>()
    private var pathClickListener : ((PathInfo)->Boolean)? = null
    val paths = mutableListOf<PathInfo>()
    val pathMap = mutableMapOf<Polyline, PathInfo>()

    private var pathInfoWindowMarker : Marker? = null
    override fun onPolylineClick(poly: Polyline?) {
        if(null==pathMap[poly]) return

        val clickedPath = pathMap[poly]!!
        val isConsumed = pathClickListener?.invoke(clickedPath)

        if(isConsumed==false){
            val anchor = pathMap[poly]?.middlePoint ?: pathMap[poly]?.startPoint!!
            pathInfoWindowMarker?.position = LatLng(anchor.latitude,anchor.longitude)
            pathInfoWindowMarker?.title ="部份路徑"
            pathInfoWindowMarker?.snippet =
                "路徑長: %.2f m\n".format(clickedPath.dist) +
                "時長: %d s\n".format(clickedPath.time/1000) +
                "順時速率: %.2f km/h \n".format(clickedPath.speed) +
                "海拔: %.2f m".format(anchor.elevation)

            pathInfoWindowMarker?.showInfoWindow()

        }
    }

    fun preparePathInfoWindow(){
        pathInfoWindowMarker = map.addMarker(
            MarkerOptions().position(LatLng(0e10,0e10))
                .infoWindowAnchor(0f,0f)
                .anchor(0f,0f)
                .icon(null)
                .alpha(0f)
        )
    }

    fun setOnPathClickListener(callback:(PathInfo)->Boolean){
        pathClickListener = callback
    }

    fun addTrackPoint(
        latitude: Double,
        longitude: Double,
        elevation: Double,
        time: Long
    ){
        trackPoints.add(
            TrackPoint(
                latitude,
                longitude,
                elevation,
                time
            )
        )
    }

    fun render(){
        preparePathInfoWindow()
        generatePath()

    }

    fun clearTrack(){
        pathInfoWindowMarker?.remove()
        pathInfoWindowMarker = null
        paths.clear()
        trackPoints.clear()
    }

    private fun generatePath(){
        paths.clear()

        val numPoints = trackPoints.size
        var pointsPerPath = numPoints/maxNumPath
        if (numPoints % maxNumPath != 0) pointsPerPath += 1

        Timber.tag("myMap").d("trk Size: $numPoints")
        Timber.tag("myMap").d("pointsPerPath: $pointsPerPath")

        var pointsIterator = 0
        while(pointsIterator < (numPoints -1)){
            val polyOption = PolylineOptions()
            polyOption.width(20f) //px
            polyOption.clickable(true)

            var pointsForPath = pointsPerPath
            if((numPoints - pointsIterator -1 )<pointsPerPath){
                pointsForPath = numPoints - pointsIterator - 1
                Timber.tag("myMap").d("final pointsPerPath: $pointsForPath")
            }

            // generate one path, consume `pointsForPath` points
            for(j in 0 until (pointsForPath+1)){
                val pointLatLng = LatLng(
                    trackPoints[pointsIterator+j].latitude,
                    trackPoints[pointsIterator+j].longitude
                )
                polyOption.add(pointLatLng)
            }
            val startPoint = trackPoints[pointsIterator]
            var middlePoint:TrackPoint? = null
            if (pointsForPath>1)
                middlePoint = trackPoints[pointsIterator+pointsForPath/2]
            val endPoint = trackPoints[pointsIterator+pointsForPath]

            val timeInterval = endPoint.time - startPoint.time

            val poly = map.addPolyline(polyOption)
            val path = PathInfo(poly,timeInterval,startPoint,middlePoint,endPoint)

            pathMap[poly] = path

            path.polyLine.color = ColorScaleLegendView.valueToColor(
                path.speed.toFloat(),0f,80f
            )

            paths.add(path)
            pointsIterator+= pointsPerPath
        }
    }

    fun showColorLegend(
        container:ViewGroup,
        inflater: LayoutInflater
    ){
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        //val b = Bitmap()

        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK

        val canvas = Canvas()
        canvas.drawRect(0f,0f,1f,1f,paint)

        canvas
        //container.addView()
    }

}

data class TrackPoint(
    var latitude: Double,
    var longitude: Double,
    var elevation: Double,
    var time: Long //in millis
)

class PathInfo(
    val polyLine : Polyline,
    val time :Long,
    val startPoint : TrackPoint,
    val middlePoint : TrackPoint? = null,
    val endPoint : TrackPoint
){
    val dist :Double = SphericalUtil.computeLength(polyLine.points)
    val speed : Double = dist/time * 3600 // in km/h
}