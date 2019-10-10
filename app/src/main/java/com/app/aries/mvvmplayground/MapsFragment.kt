package com.app.aries.mvvmplayground


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.aries.mvvmplayground.viewmodel.ViewModelFactory
import com.app.aries.mvvmplayground.viewmodel.map.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import android.location.LocationManager
import android.util.Base64
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.app.aries.mvvmplayground.map_utilities.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterManager
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import kotlinx.android.synthetic.main.fragment_maps.view.*
import org.xmlpull.v1.XmlPullParserException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.PermissionRequest
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var rootView:View
    private var mMap: GoogleMap? = null

    private lateinit var viewModel : MapViewModel

    init {
        Timber.tag("init").d("MapsFragment created!")
    }

    class permissionRequestCodeDict{
        companion object{
            const val FIRST_TIME = 1
            const val OPERATION_TIME = 2
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false)

        viewModel = ViewModelProviders
            .of(this,ViewModelFactory())
            .get(MapViewModel::class.java)

        val mapFragment = this.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        rootView.testFloatingButton.setOnClickListener {
            preResetCamera()
        }

        rootView.readGpxFloatingButton.setOnClickListener {
            clickGPXButton()
        }

        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap?.uiSettings?.setAllGesturesEnabled(true)
        setupPermission(permissionRequestCodeDict.FIRST_TIME)

        val clusterManager = ClusterManager<MarkerItem>(this.context,mMap)
        val a = clusterManager.algorithm

        mMap?.setOnCameraIdleListener(clusterManager)
        mMap?.setOnMarkerClickListener(clusterManager)
        mMap?.setOnInfoWindowClickListener(clusterManager) // ?

        clusterManager.setOnClusterInfoWindowClickListener {
            Timber.tag("myMap").d("clusterManager setOnClusterInfoWindowClickListener")
        }

        clusterManager.setOnClusterItemClickListener {
            Timber.tag("myMap").d("clusterManager setOnClusterItemClickListener")

            false
        }

        clusterManager.setOnClusterClickListener {
            Timber.tag("myMap").d("clusterManager OnClusterClickListener")

            false
        }

        viewModel.stpData.observe(this, Observer{
//            mMap?.clear()
            clusterManager.clearItems()

            for (stp in it){
//                val marker =
//                mMap?.addMarker(
//                    MarkerOptions()
//                        .position(LatLng(stp.latitude,stp.longitude))
//                        .title(stp.id.toString() + ". " + stp.position)
//                        .snippet(stp.direction)
//                )

                clusterManager.addItem(
                    MarkerItem(LatLng(stp.latitude,stp.longitude))
                        .title(stp.id.toString() + ". " + stp.position)
                        .snippet(stp.direction)
                )

            }
        })
        Timber.tag("MapViewModel").d("pre getSpeedTestPositionData")
        viewModel.getSpeedTestPositionData()
    }

    private val permissionCallbacks = object: EasyPermissions.PermissionCallbacks {
        override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
            Timber.tag("permissionLog").d("onPermissionsDenied")

            when(requestCode){
                permissionRequestCodeDict.OPERATION_TIME->{
                    if (EasyPermissions.somePermissionPermanentlyDenied(this@MapsFragment, perms)) {
                        Timber.tag("permissionLog").d("somePermissionPermanentlyDenied")
                        AppSettingsDialog.Builder(this@MapsFragment)
                            .setRationale("why you reject us forever?")
                            .build().show()
                    }
                }
            }
        }

        override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
            Timber.tag("permissionLog").d("onPermissionsGranted")
            when(requestCode){
                permissionRequestCodeDict.FIRST_TIME->{
                    Timber.tag("permissionLog").d("FIRST_TIME")
                    setupMapWithPermission()
                    resetCamera()
                }
                permissionRequestCodeDict.OPERATION_TIME->{
                    Timber.tag("permissionLog").d("OPERATION_TIME")
                    setupMapWithPermission()
                    resetCamera()
                }
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            Timber.tag("permissionLog").d("onRequestPermissionsResult")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // check permission
            setupMapWithPermission()
            resetCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this.permissionCallbacks)
    }

    private fun setupPermission(requestCode: Int){
        Timber.tag("permissionLog").d("set up permissions")
        if(!EasyPermissions.hasPermissions(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)){
            EasyPermissions.requestPermissions(this, "we need some permission",
                requestCode, Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            setupMapWithPermission()
            resetCamera()
        }
    }

    private fun setupMapWithPermission(){
        Timber.tag("permissionLog").d("oh ya! permission callback! FIRST_TIME")
        if(EasyPermissions.hasPermissions(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION) &&
            null!=mMap){
                @SuppressWarnings
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
        }
    }

    private val locationListener = object: LocationListener{
        override fun onLocationChanged(location: Location?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }
    }

    private fun getCurrentLocation() : Location? {
        if(!EasyPermissions.hasPermissions(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)) return null

        val mLocationManager = this.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        var location: Location? = null
        @SuppressWarnings
        if (!(isGPSEnabled || isNetworkEnabled))
            Timber.tag("myMap").d("no gps and wifi to get current position!")
        else {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    10f,
                    locationListener
                )
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            } else if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    10f,
                    locationListener
                )
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            }
        }
        Timber.tag("myMap").d("getCurrentLocation $location")
        return location
    }

    private fun preResetCamera(){
        if(!EasyPermissions.hasPermissions(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION))
            setupPermission(permissionRequestCodeDict.OPERATION_TIME)
        else
            resetCamera()
    }

    private fun resetCamera(){
        val l = getCurrentLocation()

        if (null!=l && null != mMap){
            Timber.tag("myMap").d("camera move to myLocation")
//            mMap?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(l.latitude,l.longitude)))
//            mMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))

            val move = RxMapView.animateCamera(mMap!!,CameraUpdateFactory.newLatLng(LatLng(l.latitude,l.longitude)))
            val zoomIn = RxMapView.animateCamera(mMap!!,CameraUpdateFactory.zoomTo(15f))

            move.flatMap<Unit>{zoomIn}.subscribe()
        }
    }

    private fun clickGPXButton(){
        val disposable = RxGpsParser.parse(
//            "gps/moGPXTracker_20190208145517.gpx",
            "gps/moGPXTracker_20190209160640.gpx",
            this.context!!
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe (
                {parsed ->
                    val trkps = parsed.tracks[0].trackSegments[0].trackPoints

                    val manager = ColoredPathManager(this@MapsFragment.context!!, mMap!!)

                    mMap?.setInfoWindowAdapter(MapInfoWindowAdapter(this.layoutInflater))

                    mMap?.setOnPolylineClickListener(manager)

                    manager.setOnPathClickListener {
                        Timber.tag("myMap").d("path click - dist:%.2f time:%d speed:%.2f".format(it.dist,it.time,it.speed))

                        false
                    }

                    for (i in trkps){
                        manager.addTrackPoint(
                            i.latitude,
                            i.longitude,
                            i.elevation,
                            i.time.millis
                        )
                    }


                    Timber.tag("myMap").d("trk Size: ${trkps.size}")

                    manager.render()

                    Timber.tag("myMap").d("Path Size: ${manager.paths.size}")

                val move = RxMapView.animateCamera(mMap!!,CameraUpdateFactory.newLatLng(LatLng(parsed.tracks[0].trackSegments[0].trackPoints[0].latitude,parsed.tracks[0].trackSegments[0].trackPoints[0].longitude)))
                move.subscribe()
                }
            ){
                Timber.tag("myMap").d("Error $it")
            }


    }

    companion object {
        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}
