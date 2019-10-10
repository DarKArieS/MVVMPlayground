package com.app.aries.mvvmplayground.map_utilities

import android.Manifest
import android.app.Activity
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

@Deprecated("not a completed class :p ")
class RxPermission {
    private lateinit var mBuilder: PermissionRequest.Builder

    private var mRequestCode: Int

    constructor(activity: Activity, requestCode: Int, vararg perms: String) {
        mRequestCode = requestCode
        mBuilder = PermissionRequest.Builder(activity, requestCode, *perms)
    }

    constructor(fragment: Fragment, requestCode: Int, vararg perms: String) {
        mRequestCode = requestCode
        mBuilder = PermissionRequest.Builder(fragment, requestCode, *perms)
        mBuilder
    }

    // only manager for granted
    private var onPermissionsGrantedCallback = {}

    fun setGrantedCallbacks(
        onPermissionsGranted: () -> Unit = {}
    ) {
        onPermissionsGrantedCallback = onPermissionsGranted
    }


    var permissionCallbacks = object: EasyPermissions.PermissionCallbacks{
        override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

        override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {}
    }

    fun getPermission(): Observable<Unit> {

        return Observable.create<Unit>{

        }
    }

}