package com.pedpo.pedporent.view.store

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityAddresStoreBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.branche.add.AddAddressBrancheTO
import com.pedpo.pedporent.model.store.branche.AddressBranchData
import com.pedpo.pedporent.model.store.branche.add.AddBranchData
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GpsUtilsKtx
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.Permission
import com.pedpo.pedporent.view.ShowMapActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.store.branch.TimeBranchStoreActivity
import com.pedpo.pedporent.viewModel.BrancheStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddressStoreActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var permission: Permission;
    @Inject
    lateinit var showProgressBar: ShowProgressBar

    private val viewModel : BrancheStoreViewModel by viewModels();

    lateinit var binding: ActivityAddresStoreBinding;

    private var lat: String? = null;
    private var lng: String? = null;
    private var googleMap: GoogleMap? = null;
    private var brancheID:String?=null;


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddresStoreBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this;
        setContentView(binding.root);
        binding.listener = this;


        brancheID = intent.getStringExtra(ContextApp.BRANCHE_ID)?:""


        if (!brancheID.isNullOrEmpty())
            getBranch()



        onClickAppBar()
        showMap()


        focusEditText(binding.eName,binding.inputName)
        focusEditText(binding.eAddress,binding.inputAddress)
    }


    /* OnClick */
    fun onClickLocation(view: View) {
        activityResultLauncherMap.launch(
            Intent(
                this@AddressStoreActivity, ShowMapActivity::class.java
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
                                this@AddressStoreActivity, ShowMapActivity::class.java
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
                binding.constraintLocation.setBackgroundResource(R.drawable.border_area)
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

        if (binding.eName.text.toString().isEmpty())
        binding.inputName.error = getString(R.string.enter_this_item);
        if (binding.eAddress.text.toString().isEmpty())
        binding.inputAddress.error = getString(R.string.enter_this_item);
        if (lat.isNullOrEmpty() || lng.isNullOrEmpty())
            binding.constraintLocation.setBackgroundResource(R.drawable.border_area_red_error)

        if (binding.eName.text.toString().isEmpty() || binding.eAddress.text.toString().isEmpty()
            || lat.isNullOrEmpty() || lng.isNullOrEmpty())
            return


        if (brancheID.isNullOrEmpty())
            createBranche()
        else
            editBranch()
    }
    private fun focusEditText(
        editText: TextInputEditText,
        inputLayout: TextInputLayout
    ) {
        editText.setOnFocusChangeListener { _, b ->
            when (b) {
                true -> {
                    inputLayout.isErrorEnabled = false;
                }
                false ->{

                }
            }
        }
        editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputLayout.isErrorEnabled = false;
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    fun createBranche(){

        Log.i("testAddressBranche", "onClickbtnConfirm:" +
                " ${binding.eName.text.toString()} " +
                " ${binding.eAddress.text.toString()} " +
                " $lat " +
                " $lng " +
                "")

        val addAddressBrancheTO = AddAddressBrancheTO(
            branchID = brancheID,
            name = binding.eName.text.toString() ,
            address = binding.eAddress.text.toString() ,
            latitude = lat?:"" ,
            longitude = lng?:""
        );

        showProgressBar.show(supportFragmentManager)


        viewModel.addAdressBranche(addAddressBrancheTO = addAddressBrancheTO)
            ?.observe(this,CustomObserver(object : CustomObserver.ResultListener<AddBranchData>{
                override fun onSuccess(dataInput: AddBranchData) {
                    showProgressBar.dismiss();

                    Log.i("testAddressBranche", "onSuccess: ${dataInput.isSuccess}")
                    Log.i("testAddressBranche", "onSuccess: ${dataInput.message}")

                    if (dataInput.isSuccess == true){
                        val intent = Intent(this@AddressStoreActivity, TimeBranchStoreActivity::class.java);
                        intent.putExtra(ContextApp.BRANCHE_ID,dataInput.data)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@AddressStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onException(exception: Exception) {
                    Toast.makeText(this@AddressStoreActivity,exception.message.toString(),Toast.LENGTH_SHORT).show()
                    Log.i("testAddressBranche", "onException: ${exception.message}")

                }
            }))
    }

    fun editBranch(){
        Log.i("testAddressBranche", "onClickbtnConfirm: " +
                "$brancheID" +
                " ${binding.eName.text.toString()} " +
                " ${binding.eAddress.text.toString()} " +
                " $lat " +
                " $lng " +
                "")
        val addAddressBrancheTO = AddAddressBrancheTO(
            branchID = brancheID,
            name = binding.eName.text.toString() ,
            address = binding.eAddress.text.toString() ,
            latitude = lat ?:"" ,
            longitude = lng ?:""
        );

        showProgressBar.show(supportFragmentManager)

        viewModel.editAdressBranche(addAddressBrancheTO = addAddressBrancheTO)
            ?.observe(this,CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()

                    Log.i("testAddressBranche", "onSuccess: ${dataInput.isSuccess}")
                    Log.i("testAddressBranche", "onSuccess: ${dataInput.message}")

                    if (dataInput.isSuccess == true){
                            finish()
                    }else{
                        Toast.makeText(this@AddressStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onException(exception: Exception) {
                    Toast.makeText(this@AddressStoreActivity,exception.message.toString(),Toast.LENGTH_SHORT).show()
                    Log.i("testAddressBranche", "onException: ${exception.message}")

                }
            }))
    }

    fun getBranch(){
            viewModel.getAdressBranche(branchID = brancheID?:"")?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<AddressBranchData>{
                override fun onSuccess(dataInput: AddressBranchData) {
                    Log.e("testAddressBranche", "onSuccess: ${dataInput.isSuccess}")
                    binding.eName.setText( dataInput.data?.name)
                    binding.eAddress.setText( dataInput.data?.address)

                    if (!dataInput.data?.latitude.isNullOrEmpty() && !dataInput.data?.longitude.isNullOrEmpty()) {
                        binding.carViewMap.isVisible = true;
                        lat = dataInput.data?.latitude;
                        lng = dataInput.data?.longitude;
                        placeMap(
                            LatLng(dataInput.data?.latitude?.toDouble() ?: 29.4963 ,
                                dataInput.data?.longitude?.toDouble() ?: 60.8629
                            ), 14f
                        )
                    }

                }

                override fun onException(exception: Exception) {
                    Log.e("testAddressBranche", "onSuccess: ${exception.message}")

                }
            }))
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


    fun showMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
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

        googleMap?.addMarker(
            MarkerOptions()
                .position(latLng))

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        googleMap?.setOnMapClickListener {
            activityResultLauncherMap.launch(Intent(this, ShowMapActivity::class.java))
        }
    }



}