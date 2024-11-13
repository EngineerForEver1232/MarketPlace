package com.pedpo.pedporent

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pedpo.pedporent.databinding.ActivityMapOsmBinding
import com.pedpo.pedporent.model.model.MapData
import com.pedpo.pedporent.model.model.MapResponse
import com.pedpo.pedporent.model.model.RequestMapTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.view.adapter.AdapterMap
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.viewModel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject


@AndroidEntryPoint
class MapOsmActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapOsmBinding

    private var mapController: IMapController?=null

    private val viewModel: MapViewModel by viewModels()
    private val viewModelDetails: DetailsViewModel by viewModels();

    @Inject
    lateinit var adapter: AdapterMap
    private var cityID: String? = null;
    private var city: String? = null;

    private var list: ArrayList<MapData>? = null
    private var typeApi = ContextApp.RENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapOsmBinding.inflate(layoutInflater)
        val ctx: Context = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(binding.root)

        Dexter.withContext(applicationContext).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                }
                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest?>?, permissionToken: PermissionToken?
                ) {

                }
            }).check()


        mapController = binding.mapView.controller

        mapController?.setZoom(15L.toDouble())

        binding.mapView.isTilesScaledToDpi = false

        binding.mapView.setBuiltInZoomControls(true)

        binding.mapView.setMultiTouchControls(true)

        val point = GeoPoint(
            43.651070,
            -79.347015
        )

        mapController?.setCenter(point);


        val marker = Marker(binding.mapView)
        marker.position

        Log.i("testOsmMap", "onCreate: " +
                "actualNorth : ${binding.mapView.boundingBox.actualNorth}" +
                " actualSouth ${binding.mapView.boundingBox.actualSouth}" +
                "")

        binding.btn.setOnClickListener {
            Log.i("testOsmMap", "onCreate: " +
                    "actualNorth : ${binding.mapView.boundingBox.actualNorth}" +
                    " actualSouth ${binding.mapView.boundingBox.actualSouth}" +
                    "")
        }

    }

    fun markets(
        typeApi: String,
        cityID: String = "97170659-936A-4A71-8604-2A797BE359CA",
        latSouthwest: Double,
        lngSouthwest: Double,
        latNortheast: Double,
        lngNortheast: Double
    ) {
        viewModel.markets(
            requestMapTO = RequestMapTO(
                cityID = cityID ?: "97170659-936A-4A71-8604-2A797BE359CA",
                categoryID = null,
                iP = GettingIP(this@MapOsmActivity).deviceIpAddress,
                type = typeApi,
                latSouthwest = latSouthwest,
                lngSouthwest = lngSouthwest,
                latNortheast = latNortheast,
                lngNortheast = lngNortheast
            )
        ).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<MapResponse> {
                override fun onSuccess(dataInput: MapResponse) {

                    Log.i("testMap", "onCreate: ${dataInput.isSuccess}")
                    if (dataInput.isSuccess == true) {
                        if (!dataInput.data.isNullOrEmpty()) {
//                                moveCamera(
//                                    LatLng(
//                                        dataInput.data?.get(0)?.latitude ?: 51.213890,
//                                        dataInput.data?.get(0)?.longitude ?: -102.462776
//                                    )
                            list = dataInput.data
                            initPagerPoster()

                            adapter.update(list = dataInput.data ?: arrayListOf())
                            showPinInMap()

                            mapController?.setCenter(GeoPoint(
                                dataInput.data?.get(0)?.latitude?:43.651070 ,
                                dataInput.data?.get(0)?.longitude?:(-79.347015))
                            )

                            val marker = Marker(binding.mapView)
                            marker.setTextIcon("Icon")

                            marker.position = GeoPoint(
                                dataInput.data?.get(0)?.latitude?:43.651070 ,
                                dataInput.data?.get(0)?.longitude?:(-79.347015)
                            )

                            binding.mapView.overlays.add(marker);


                        } else {
                            adapter.update(arrayListOf())
                        }

                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    private fun initPagerPoster() {

        binding.viewPagerPoster.offscreenPageLimit = 3;
        binding.viewPagerPoster.adapter = adapter;
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
        binding.viewPagerPoster.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if (binding.viewPagerPoster.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(binding.viewPagerPoster) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset;
                } else {
                    page.translationX = myOffset;
                }
            } else {
                page.translationY = myOffset;
            }
        }

        viewPagerScale()
    }

    private fun viewPagerScale() {
        binding.viewPagerPoster.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                gotoLatLng_Trades2(
                    LatLng(
                        list?.get(position)?.latitude ?: 51.213890,
                        list?.get(position)?.longitude ?: -102.462776
                    ), position
                )
            }
        })
    }

    private fun gotoLatLng_Trades2(latLng: LatLng, position: Int) {
        /** change icon before , after : marketOld itemi hast k qablan choose shode va alan bayad  halae disSelected shavad **/
//        if (markerOld != null)
//            changeIconMarker(marker = markerOld, false)
//
//        if (!listMarker.isNullOrEmpty()) {
//            markerOld = listMarker?.get(position)
//            changeIconMarker(listMarker?.get(position), true)
//        }

    }

    fun showPinInMap() {
        list?.forEachIndexed { index, testTO ->
//            val marketOption = MarkerOptions().position(LatLng(testTO.latitude, testTO.longitude))
//            val markerMap = googleMap?.addMarker(marketOption)

            val marker = Marker(binding.mapView)
            marker.setTextIcon("Icon")

            marker.position = GeoPoint(testTO.latitude, -testTO.longitude)

            binding.mapView.overlays.add(marker);
//            marker?.tag = index
//            listMarker?.add(markerMap!!)
//            changeIconMarker(markerMap, false)
        }
    }

}