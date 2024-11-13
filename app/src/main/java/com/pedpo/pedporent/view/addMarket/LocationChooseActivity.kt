package com.pedpo.pedporent.view.addMarket

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityLocationChooseBinding
import com.pedpo.pedporent.utills.permission.Permission
import com.pedpo.pedporent.view.ShowMapActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pedpo.pedporent.model.market.editMarket.EditMarketTO
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.utills.*

import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.view.dialog.ShowAreaDialog


@AndroidEntryPoint
class LocationChooseActivity : AppCompatActivity(), OnReturnPlace, OnMapReadyCallback {

    @Inject
    lateinit var permission: Permission;

    lateinit var binding: ActivityLocationChooseBinding;

    private var market: EditMarketTO? = null;

    private var lat: String? = null;
    private var lng: String? = null;
    private var cityID: String? = null;
    private var city: String? = null;
    var typeMarket: String? = null;
    private var isStore: Boolean? = null;



    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationChooseBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this;
        setContentView(binding.root)
        binding.listener = this

        market = intent.getParcelableExtra<EditMarketTO>(ContextApp.MARKET);
        isStore = intent.getBooleanExtra(ContextApp.STORE,false)


        typeMarket = intent.getStringExtra(ContextApp.TYPE_MARKET) ?: ""

        when (typeMarket) {
            ContextApp.RENT -> binding?.actionBar.tLabelBar.text =
                "${getString(R.string.rent)} - ${getString(R.string.ad_registration)}"
            ContextApp.SALE -> binding?.actionBar.tLabelBar.text =
                "${getString(R.string.sell)} - ${getString(R.string.ad_registration)}"
            else -> binding?.actionBar.tLabelBar.text =
                "${getString(R.string.service)} - ${getString(R.string.ad_registration)}"
        }

        binding.tAddress.text = market?.cityName;
        cityID = market?.cityID;
        lng = market?.longitude;
        lat = market?.latitude;

        onClickAppBar()
        switchBtn()
        showMap()

        if (isStore == true) {
            binding.switchBtn.isVisible = false;
            binding.switchBtn.isChecked = true;
            binding.constraintLocation.isEnabled = true;

        }

    }


    /* CheckedChande */
    fun switchBtn() {
        binding.constraintLocation.isEnabled = false;

        binding.switchBtn.setOnCheckedChangeListener { _, bool ->
            when (bool) {
                true -> {
                    binding.icLocation.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_standard
                        )
                    )
                    binding.tLocation.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_standard
                        )
                    )
                    binding.constraintLocation.setBackgroundResource(R.drawable.border_area)
                    binding.constraintLocation.isEnabled = true;
                }
                false -> {
                    binding.icLocation.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.gray_light
                        )
                    )
                    binding.tLocation.setTextColor(ContextCompat.getColor(this, R.color.gray_light))
                    binding.constraintLocation.setBackgroundResource(R.drawable.border_area_light)
                    binding.constraintLocation.isEnabled = false;
                }
            }
        }
    }

    /* OnClick */
    fun onClickLocation(view: View) {
        activityResultLauncherMap.launch(
            Intent(
                this@LocationChooseActivity, ShowMapActivity::class.java
            )
        )
//            checkPermission()
    }


    fun checkPermission() {
        permission.initAc(this, context = this)
        when (permission.checkPermissionGallery(
            Manifest.permission.ACCESS_FINE_LOCATION,
            ContextApp.LOCATION
        )) {
            ContextApp.TRUE -> {
                goToSettingLocation()
            }
            ContextApp.REQUEST_PERMISSION ->
                reqPermissionLocation()
            ContextApp.SHOULD_SHOW_RequestPermissionRationale ->
                showAlertDialog(
                    this?.getString(R.string.request_permission)!!,
                    this?.getString(R.string.should_permission_Location)!!,
                    ContextApp.LOCATION
                )
            ContextApp.SETTING ->
                showAlertDialog(
                    this?.getString(R.string.confirm_pemissions)!!,
                    this?.getString(R.string.please_confirm_permission_setting)!!,
                    ContextApp.SETTING
                )
        }
    }

    fun reqPermissionLocation() {
        permissionResultFindLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val permissionResultFindLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //open location
                goToSettingLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_confirm),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

//    var activityResultLauncherSettings =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//            }
//        }


    fun goToSettingLocation() {

        GpsUtilsKtx(this, activityResultLauncherLocException)
            .turnGPSOn(object : GpsUtilsKtx.OnGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    if (isGPSEnable) {
//                        activityResultLauncherMap.launch(Intent(this, ActivityShowMap::class.java))
                        activityResultLauncherMap.launch(
                            Intent(
                                this@LocationChooseActivity, ShowMapActivity::class.java
                            )
                        )
                    } else {
                        activityResultLauncherSettingsLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }

            })
    }

    var activityResultLauncherLocException: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Your logic
                activityResultLauncherMap.launch(Intent(this, ShowMapActivity::class.java))

            } else {

            }
        }


    var activityResultLauncherMap =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lat = result.data?.getStringExtra(ContextApp.LATITUDE)
                lng = result.data?.getStringExtra(ContextApp.LONGITUDE)

                binding.carViewMap.isVisible = true;
                binding.constraintLocation.setBackgroundResource(R.drawable.border_area_light)
                placeMap(LatLng(lat?.toDouble() ?: 29.4963, lng?.toDouble() ?: 60.8629), 14f)

            } else {

            }
        }

    var activityResultLauncherSettingsLocation =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (permission.isCheckPermissionGallery(Manifest.permission.ACCESS_FINE_LOCATION)) {
                goToSettingLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_confirm),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

    /*OnClick*/
    fun onClickbtnConfirm(view: View) {
        if (city.isNullOrEmpty()) {
            binding.constraintAddress.setBackgroundResource(R.drawable.border_stroke_error)
        }

        if (lat.isNullOrEmpty() && lng.isNullOrEmpty())
            binding.constraintLocation.setBackgroundResource(R.drawable.border_area_red_error)

        if (city.isNullOrEmpty())
            return
        if (lat.isNullOrEmpty() && lng.isNullOrEmpty())
             return

        val intent = Intent()
        intent.putExtra(ContextApp.CITY, binding.tAddress.text)
        intent.putExtra(ContextApp.CITY_ID, cityID)
        intent.putExtra(ContextApp.LATITUDE, lat)
        intent.putExtra(ContextApp.LONGITUDE, lng)

        setResult(RESULT_OK, intent)
        finish()

    }

    /*OnClick*/
    fun onClickCity(view: View) {
        val showAreaDialog = ShowAreaDialog();
        showAreaDialog.onReturnPlace = this;
        showAreaDialog.show(supportFragmentManager, "area")
    }

    /*OnClick*/
    private fun onClickAppBar() {
        binding.actionBar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.actionBar.icClose.setOnClickListener {
            setResult(ContextApp.FINISH)
            finish()
        }
    }

    fun showAlertDialog(title: String, message: String, type: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.i_agree) { dialog, i ->
                run {
                    dialog.dismiss()
                    if (type.equals(ContextApp.SETTING)) {
                        val mIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val packageUri: Uri =
                            Uri.fromParts("package", this.packageName, null)
                        mIntent.data = packageUri
                        activityResultLauncherSettingsLocation?.launch(mIntent)
                    } else if (type.equals(ContextApp.LOCATION)) {
                        reqPermissionLocation()
                    } else if (type.equals(ContextApp.GALLERY)) {
//                        reqPermissionGallery()
                    }

                }
            }.setNegativeButton(R.string.cancel) { dialogInterface, i ->
                run {
                    dialogInterface.dismiss()
                }
            }.create()
            .show();
    }


    override fun onReturnPlace(placeTO: PlaceTO) {
        binding.tAddress.text = placeTO.name;
        city = placeTO.name;
        cityID = placeTO.id;
        binding.constraintAddress.setBackgroundResource(R.drawable.border_search_white)
    }

    var googleMap: GoogleMap? = null;

    fun showMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap;
        placeMap(LatLng(29.4963, 60.8629), 10f)

    }

    fun placeMap(latLng: LatLng, zoom: Float) {

        googleMap?.clear()
        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to Mountain View
            .zoom(zoom)
            .build() // Creates a CameraPosition from the builder

        googleMap?.addMarker(MarkerOptions().position(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        googleMap?.setOnMapClickListener {
            activityResultLauncherMap.launch(Intent(this, ShowMapActivity::class.java))
        }
    }



}