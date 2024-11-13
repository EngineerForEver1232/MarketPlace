package com.pedpo.pedporent.utills.permission

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pedpo.pedporent.utills.ContextApp
import javax.inject.Inject

class Permission @Inject constructor(private val permissionStatus: SharedPreferences) {


    private var fragmentDialog: Activity?=null
    private var context:Context?=null

    fun initAc(fragmentDialog: Activity,context: Context){
        this.fragmentDialog = fragmentDialog
        this.context = context
    }

    fun checkPermissionGallery(permission: String,type: String):String {
        return if (ContextCompat.checkSelfPermission(
                context!!,
                permission
            ) != PackageManager.PERMISSION_GRANTED) {
            shouldRequestPermissionGallery(permission,type)
        } else {
            ContextApp.TRUE
        }
    }
    fun isCheckPermissionGallery(permission: String):Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldRequestPermissionGallery(permission:String,type:String):String {

        val result = if (ActivityCompat.shouldShowRequestPermissionRationale(
                fragmentDialog!!,
                permission
            )
        ) {
             ContextApp.SHOULD_SHOW_RequestPermissionRationale
        } else if (permissionStatus.getBoolean(type, false)) {
             ContextApp.SETTING
        }else{
             ContextApp.REQUEST_PERMISSION
        }

        val shEditor = permissionStatus.edit()
        shEditor?.putBoolean(type, true)
        shEditor?.apply()

        return result
    }
}