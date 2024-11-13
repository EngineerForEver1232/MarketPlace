package com.pedpo.pedporent.utills

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class GpsUtilsKtx {

    private var context: Context? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null


    constructor(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        this.context = context
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mSettingsClient = LocationServices.getSettingsClient(context)
        locationRequest = LocationRequest.create()
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        // interval : speed update kardan location . dar in project b kar nmiyad
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
        this.launcher = launcher;
    }

    fun turnGPSOn(onGpsListener: OnGpsListener) {
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)
        } else {
            mSettingsClient
                ?.checkLocationSettings(mLocationSettingsRequest) // vase zamani k ok ra mizanad
                ?.addOnSuccessListener((context as Activity?)!!) { //  GPS is already enable, callback GPS status through listener
                    onGpsListener?.gpsStatus(true)
                } // vase zamani k location on nist va bayad dynamik turn on
                ?.addOnFailureListener(
                    (context as Activity?)!!
                ) { e ->
//                    Log.e("testPermission", "onFailure: " + e.message)
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
//                            rae.startResolutionForResult(
//                                context as Activity?,
//                                AppContents.GPS_REQUEST
//                            )
                            launcher?.launch(IntentSenderRequest.Builder(
                                rae.resolution).build())

                        } catch (sie: SendIntentException) {
                            //                                        Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                            val errorMessage = "Location settings are inadequate, and cannot be " +
//                                    "fixed here. Fix in Settings."
                            //                                    Log.e(TAG, errorMessage);
                            onGpsListener?.gpsStatus(false)
                        }
                    }
                }
        }
    }


    interface OnGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }

}