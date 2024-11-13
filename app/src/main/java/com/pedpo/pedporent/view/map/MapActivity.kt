package com.pedpo.pedporent.view.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.card.MaterialCardView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterMapBinding
import com.pedpo.pedporent.databinding.MapActivityBinding
import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.model.model.MapData
import com.pedpo.pedporent.model.model.MapResponse
import com.pedpo.pedporent.model.model.OneMarketMap
import com.pedpo.pedporent.model.model.RequestMapTO
import com.pedpo.pedporent.model.place.LatLngPlace
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.AdapterMap
import com.pedpo.pedporent.view.adapter.AdapterSearchPlace
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.ShowAreaDialog
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.viewModel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    OnMarkerClickListener, AdapterMap.ClickAdapter, OnReturnPlace {

    lateinit var binding: MapActivityBinding
    @Inject
    lateinit var adapter: AdapterMap
    @Inject
    lateinit var prefApp: PrefApp
    @Inject
    lateinit var progressBar: ShowProgressBar
    @Inject
    lateinit var utilsMap: UtilsMap

    private var googleMap: GoogleMap? = null
    private var adapterSection: AdapterSearchPlace? = null
    private val viewModel: MapViewModel by viewModels()
    private val viewModelDetails: DetailsViewModel by viewModels()

    private var listMarker: ArrayList<Marker>? = ArrayList()
    private var markerOld: Marker? = null

    private var cityID: String? = null;
    private var city: String? = null;

    private var list: ArrayList<MapData>? = null
    private var typeApi = ContextApp.RENT

    private var moveWithTouch = false;


    private var latSouthwest = 43.78612453333784
    private var latNortheast = 43.81127039719941
    private var lngSouthwest = -79.28601332008839
    private var lngNortheast = -79.26835667341948


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapActivityBinding.inflate(layoutInflater)
        binding.listener = this
        setContentView(binding.root)
        componentSearch()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        adapter.clickAdapter = this;
        initPagerPoster()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setMaxZoomPreference(16f)
        this.googleMap?.setOnMarkerClickListener(this)
//        this.googleMap?.isMyLocationEnabled = true


        moveCameraWithTouch()
        oneMarket(cityID = prefApp.getCityID())

    }

    private fun oneMarket(cityID: String = "97170659-936A-4A71-8604-2A797BE359CA") {
        progressBar.show(fragmentManager = supportFragmentManager)
        viewModel.oneMarkets(cityID = cityID)
            .observe(this,
                CustomObserver(object : CustomObserver.ResultListener<OneMarketMap> {
                    override fun onSuccess(dataInput: OneMarketMap) {
                        progressBar.dismiss()

                        moveCameraForOneMarket(
                            LatLng(
                                dataInput.data?.latitude ?: 51.213890,
                                dataInput.data?.longitude ?: -102.462776
                            )
                        )
                    }

                    override fun onException(exception: Exception) {

                        progressBar.dismiss()

                    }

                })
            )
    }

    fun moveCameraForOneMarket(latLng: LatLng?) {
        googleMap?.setMaxZoomPreference(16f)
        val cameraPosition = CameraPosition.Builder()
            .target(
                latLng ?: LatLng(43.651070, -79.347015)
            ) // Sets the center of the map to Mountain View
            .zoom(11f)
            .build() // Creates a CameraPosition from the builder

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng ?: LatLng(
                    43.651070 ,
                    -79.347015
                ), 11f
            )
        )

        val bounds = googleMap?.projection?.visibleRegion?.latLngBounds
        latSouthwest = bounds?.southwest?.latitude ?: 43.78612453333784
        latNortheast = bounds?.northeast?.latitude ?: 43.81127039719941
        lngSouthwest = bounds?.southwest?.longitude ?: -79.28601332008839
        lngNortheast = bounds?.northeast?.longitude ?: -79.26835667341948

//        markets(
//            typeApi = typeApi,
//            latSouthwest = latSouthwest,
//            latNortheast = latNortheast,
//            lngNortheast = lngNortheast,
//            lngSouthwest = lngSouthwest
//        )



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
                iP = GettingIP(this@MapActivity).deviceIpAddress,
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

    fun showPinInMap() {
        list?.forEachIndexed { index, testTO ->
            val marketOption = MarkerOptions().position(LatLng(testTO.latitude, testTO.longitude))
            val markerMap = googleMap?.addMarker(marketOption)

            markerMap?.tag = index
            listMarker?.add(markerMap!!)

            changeIconMarker(markerMap, false)
        }
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
        binding.viewPagerPoster.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                moveWithTouch = false;
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
        if (markerOld != null)
            changeIconMarker(marker = markerOld, false)

        if (!listMarker.isNullOrEmpty()) {
            markerOld = listMarker?.get(position)
            changeIconMarker(listMarker?.get(position), true)
        }
    }

    private fun changeIconMarker(marker: Marker?, selected: Boolean) {
        when (typeApi) {
            ContextApp.RENT -> {
                if (selected)
                    marker?.setIcon(
                        bitmapDescriptorFromVector(
                            this@MapActivity,
                            R.drawable.ic_map_rent_selected
                        )
                    )
                else
                    marker?.setIcon(
                        bitmapDescriptorFromVector(
                            this@MapActivity,
                            R.drawable.ic_map_rent
                        )
                    )
            }
            ContextApp.SALE -> {
                if (selected)
                    marker?.setIcon(
                        bitmapDescriptorFromVector(
                            this@MapActivity,
                            R.drawable.ic_map_sale_selected
                        )
                    )
                else
                    marker?.setIcon(
                        bitmapDescriptorFromVector(
                            this@MapActivity,
                            R.drawable.ic_map_sale
                        )
                    )
            }
            ContextApp.SERVICE -> {
                if (selected)
                    marker?.setIcon(
                        bitmapDescriptorFromVector(
                            this@MapActivity,
                            R.drawable.ic_map_service_selected
                        )
                    )
                else
                    marker?.setIcon(
                        bitmapDescriptorFromVector(
                            this@MapActivity,
                            R.drawable.ic_map_service
                        )
                    )
            }

        }
    }

    private fun componentSearch() {
        val mList = ArrayList<LatLngPlace>();
        mList.add(LatLngPlace("Toronto", LatLng(43.651070, -79.347015)))
        mList.add(LatLngPlace("Tehran", LatLng(35.715298, 51.404343)))

        adapterSection = AdapterSearchPlace(
            this,
            R.layout.text_list,
            R.id.text,
            mList
        )


    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun moveCamera(latLng: LatLng?) {
        moveWithTouch = false;
        googleMap?.setMaxZoomPreference(16f)
        val cameraPosition = CameraPosition.Builder()
            .target(
                latLng ?: LatLng(43.651070, -79.347015)
            ) // Sets the center of the map to Mountain View
            .zoom(10f)
            .build() // Creates a CameraPosition from the builder

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng ?: LatLng(
                    43.651070,
                    -79.347015
                ), 11f
            )
        )

    }

    private fun moveCameraWithTouch() {
        googleMap?.setOnCameraIdleListener(OnCameraIdleListener { // Cleaning all the markers.
//            when (moveWithTouch) {
//                true -> {
            googleMap?.clear()
            markerOld = null;
            listMarker?.clear()
            googleMap?.clear()

            val bounds = googleMap?.projection?.visibleRegion?.latLngBounds
            latSouthwest = bounds?.southwest?.latitude ?: 43.78612453333784
            latNortheast = bounds?.northeast?.latitude ?: 43.81127039719941
            lngSouthwest = bounds?.southwest?.longitude ?: -79.28601332008839
            lngNortheast = bounds?.northeast?.longitude ?: -79.26835667341948

            markets(
                typeApi = typeApi,
                latSouthwest = latSouthwest,
                latNortheast = latNortheast,
                lngNortheast = lngNortheast,
                lngSouthwest = lngSouthwest
            )
//                }
//            }

            Log.i(
                "testLocation",
                "Idle latLngBounds: ${googleMap?.projection?.visibleRegion?.latLngBounds}"
            )

        })
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

    override fun onMapClick(p0: LatLng) {

    }

    /* OnClick Back */
    fun onClickBack(view: View) {
        onBackPressed()
    }

    /* OnClick Sale */
    fun onClickSale(view: View) {
        utilsMap.cardSelected(binding.cardSale, binding.imgSale, this@MapActivity)

        utilsMap.cardUnSelected(binding.cardRent, binding.imgRent, this@MapActivity)
        utilsMap.cardUnSelected(binding.cardService, binding.imgService, this@MapActivity)


        markerOld = null;
        listMarker?.clear()
        googleMap?.clear()
        typeApi = ContextApp.SALE
        markets(
            typeApi = ContextApp.SALE,
            latSouthwest = latSouthwest, latNortheast = latNortheast,
            lngSouthwest = lngSouthwest, lngNortheast = lngNortheast
        )
    }

    /* Onclick Rent */
    fun onClickRent(view: View) {
        utilsMap.cardSelected(binding.cardRent, binding.imgRent, this@MapActivity)

        utilsMap.cardUnSelected(binding.cardSale, binding.imgSale, this@MapActivity)
        utilsMap.cardUnSelected(binding.cardService, binding.imgService, this@MapActivity)


        markerOld = null;
        listMarker?.clear()
        googleMap?.clear()
        typeApi = ContextApp.RENT
        markets(
            typeApi = ContextApp.RENT,
            latSouthwest = latSouthwest, latNortheast = latNortheast,
            lngSouthwest = lngSouthwest, lngNortheast = lngNortheast
        )
    }

    /* Onclick Service */
    fun onClickService(view: View) {
        utilsMap.cardSelected(binding.cardService, binding.imgService, this@MapActivity)

        utilsMap.cardUnSelected(binding.cardSale, binding.imgSale, this@MapActivity)
        utilsMap.cardUnSelected(binding.cardRent, binding.imgRent, this@MapActivity)


        markerOld = null
        listMarker?.clear()
        googleMap?.clear()
        typeApi = ContextApp.SERVICE
        markets(
            typeApi = ContextApp.SERVICE,
            latSouthwest = latSouthwest, latNortheast = latNortheast,
            lngSouthwest = lngSouthwest, lngNortheast = lngNortheast
        )

    }

    /* Onclick TypeMap */
    fun onClickTypeMap(view: View) {
        if (googleMap?.mapType == GoogleMap.MAP_TYPE_NORMAL) {
            googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            binding.imgTypeMap.setImageResource(R.drawable.ic_map_type_sutellite)
        } else {
            googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            binding.imgTypeMap.setImageResource(R.drawable.ic_map_type_normal)
        }
    }


    /*onClick Search*/
    fun onClickSearch(view: View) {
        val showAreaDialog = ShowAreaDialog()
        showAreaDialog.onReturnPlace = this
        showAreaDialog.show(supportFragmentManager, "area")
    }

    // search city
    override fun onReturnPlace(placeTO: PlaceTO) {
        binding.tPlace.text = placeTO.name.toString()

        city = placeTO.name
        cityID = placeTO.id

        markerOld = null
        listMarker?.clear()
        googleMap?.clear()

        oneMarket(cityID = placeTO.id ?: "97170659-936A-4A71-8604-2A797BE359CA")
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker.tag != null) {
            binding.viewPagerPoster.currentItem = marker.tag.toString().toInt()
        }
        /** change icon before , after : marketOld itemi hast k
         *  qablan choose shode va alan bayad  halae disSelected shavad **/
        if (markerOld != null)
            changeIconMarker(marker = markerOld, false)
        markerOld = marker

        changeIconMarker(marker, true)
        return false
    }

    override fun onClickAdapter(mapData: MapData?, action: String, binding: AdapterMapBinding) {
        when (action) {
            ContextApp.DETAILS -> {
                val pedpo = Intent(this@MapActivity, DetailsActivity::class.java);
                pedpo.putExtra(ContextApp.MARKET_ID, mapData?.marketID)
                pedpo.putExtra(ContextApp.TYPE_API, mapData?.isService)

                startActivity(pedpo);

            }

            ContextApp.BOOKMARK -> {
                addBookmark(mapData = mapData, binding = binding);
            }
            ContextApp.LIKE -> {
                like(mapData = mapData, binding = binding);
            }
        }
    }


    fun addBookmark(mapData: MapData?, binding: AdapterMapBinding) {

        viewModelDetails.addBookmark(
            marketID = mapData?.marketID,
            if (mapData?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {


                    if (dataInput.isSuccess) {
                        MyMutable.mutableBookmark.postValue(MyMutable.BooleanBookmark(bookmark = true))
                        mapData?.isBookMarkByUser = dataInput.data?.isBookMarkByUser == true

                        if (dataInput.data?.isBookMarkByUser!!)
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked);
                        else
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmark);
                    }

                }

                override fun onException(exception: Exception) {

                    //                    Log.e("testbookmark", "onException: " + exception.message)
                }

            })
        )
    }

    fun like(mapData: MapData?, binding: AdapterMapBinding) {

        viewModelDetails.like(
            viewTO = ViewTO(
                mapData?.marketID ?: "",
                GettingIP(this@MapActivity).deviceIpAddress ?: "",
                if (mapData?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {


                    mapData?.isLikeByIp = dataInput.data?.isLikeByIP == true;


                    if (dataInput.data?.isLikeByIP!!) {
                        binding.icLike.setImageResource(R.drawable.ic_liked)
                    } else {
                        binding.icLike.setImageResource(R.drawable.ic_like)
                    }

                }

                override fun onException(exception: Exception) {
                    Log.e("testView", "onSuccess: ${exception.message}")
                }
            })
        )
    }


}