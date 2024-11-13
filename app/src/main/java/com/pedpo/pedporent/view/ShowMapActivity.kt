package com.pedpo.pedporent.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityShowMapBinding
import com.pedpo.pedporent.model.place.LatLngPlace
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.AdapterSearchPlace

class ShowMapActivity : AppCompatActivity() , OnMapReadyCallback, OnMapClickListener {

    private var googleMap: GoogleMap? = null
    private var circle: Circle? = null
    private var radius = 2
    private var vahed = 100
    lateinit var binding: ActivityShowMapBinding

    private var adapterSection:AdapterSearchPlace?=null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowMapBinding.inflate(layoutInflater)
        val view = binding.root
        binding.viewModel = this
        setContentView(view)
        initToolbar()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        val mList = ArrayList<LatLngPlace>();
        mList.add(LatLngPlace("Toronto", LatLng(43.651070, -79.347015)))
        mList.add(LatLngPlace("Tehran", LatLng(35.715298, 51.404343)))


        adapterSection = AdapterSearchPlace(
            this,
            R.layout.text_list,
            R.id.text,
            mList
        )

        binding.tAutoCompeleteNecessry.setAdapter(adapterSection)
        binding.tAutoCompeleteNecessry.setText(mList[0].cityName,false)

        binding.tAutoCompeleteNecessry.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->

                Toast.makeText(this@ShowMapActivity, mList[position].cityName,Toast.LENGTH_SHORT).show()
                moveCamera(mList[position].latlng)
            }
    }

    fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
//        if (toolbar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setMaxZoomPreference(16f)


//        val latLngIran = LatLng(29.4963, 60.8629)
        val latLngToronto = LatLng(43.651070,-79.347015)

        val cameraPosition = CameraPosition.Builder()
            .target(latLngToronto) // Sets the center of the map to Mountain View
            .zoom(10f)
            .build() // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngToronto, 12f))

    }

    fun moveCamera(latLng: LatLng?){
        googleMap?.setMaxZoomPreference(16f)


        val cameraPosition = CameraPosition.Builder()
            .target(latLng?: LatLng(43.651070,-79.347015)) // Sets the center of the map to Mountain View
            .zoom(10f)
            .build() // Creates a CameraPosition from the builder

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng?: LatLng(43.651070,-79.347015), 12f))
    }



    fun getCenter(view: View) {
        val latLng = googleMap?.cameraPosition?.target

//        checkCountry(latLng);
        val intent = Intent()
        intent.putExtra(ContextApp.LATITUDE, latLng?.latitude.toString() + "")
        intent.putExtra(ContextApp.LONGITUDE, latLng?.longitude.toString() + "")
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onMapClick(latLng: LatLng) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))

    }

}