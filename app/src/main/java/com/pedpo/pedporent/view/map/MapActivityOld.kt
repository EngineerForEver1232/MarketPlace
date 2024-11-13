//package com.pedpo.pedporent.view.map
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.AdapterView
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.core.view.ViewCompat
//import androidx.fragment.app.viewModels
//import androidx.viewpager2.widget.ViewPager2
//import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
//import com.google.android.gms.maps.GoogleMapOptions
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.*
//import com.pedpo.pedporent.R
//import com.pedpo.pedporent.databinding.AdapterMapBinding
//import com.pedpo.pedporent.databinding.AdapterPaginNewBinding
//import com.pedpo.pedporent.databinding.MapActivityBinding
//import com.pedpo.pedporent.listener.OnReturnPlace
//import com.pedpo.pedporent.model.BookmarkData
//import com.pedpo.pedporent.model.LikeData
//import com.pedpo.pedporent.model.ViewTO
//import com.pedpo.pedporent.model.model.MapData
//import com.pedpo.pedporent.model.model.MapResponse
//import com.pedpo.pedporent.model.model.RequestMapTO
//import com.pedpo.pedporent.model.place.LatLngPlace
//import com.pedpo.pedporent.model.place.PlaceTO
//import com.pedpo.pedporent.utills.*
//import com.pedpo.pedporent.view.adapter.AdapterMap
//import com.pedpo.pedporent.view.adapter.AdapterSearchPlace
//import com.pedpo.pedporent.view.details.DetailsActivity
//import com.pedpo.pedporent.view.details.DetailsNeighborActivity
//import com.pedpo.pedporent.view.dialog.ShowAreaDialog
//import com.pedpo.pedporent.view.dialog.ShowProgressBar
//import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
//import com.pedpo.pedporent.viewModel.DetailsViewModel
//import com.pedpo.pedporent.viewModel.MapViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//import kotlin.collections.ArrayList
//
//
//@AndroidEntryPoint
//class MapActivityOld : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
//    OnMarkerClickListener, AdapterMap.ClickAdapter , OnReturnPlace{
//
//    private var googleMap: GoogleMap? = null
//    private var circle: Circle? = null
//    private var radius = 2
//    private var vahed = 100
//    lateinit var binding: MapActivityBinding
//    private var adapterSection: AdapterSearchPlace? = null
//    private val viewModel: MapViewModel by viewModels()
//    private val viewModelDetails: DetailsViewModel by viewModels();
//
//    private var listMarker: ArrayList<Marker>? = ArrayList()
//    private var markerOld: Marker? = null
//
//    private var cityID: String? = null;
//    private var city: String? = null;
//
//    @Inject
//    lateinit var adapter: AdapterMap
//
//    @Inject
//    lateinit var progressBar: ShowProgressBar
//
//    private var list: ArrayList<MapData>? = null
//    private var typeApi = ContextApp.RENT
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = MapActivityBinding.inflate(layoutInflater)
//        binding.listener = this
//        setContentView(binding.root)
//        componentSearch()
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(this)
//
//        adapter.clickAdapter = this;
//        initPagerPoster()
//
//
//    }
//
//    fun markets(typeApi: String , cityID:String = "97170659-936A-4A71-8604-2A797BE359CA") {
//        progressBar.show(fragmentManager = supportFragmentManager)
//        viewModel.markets(
//            requestMapTO = RequestMapTO(
//                cityID = cityID?:"97170659-936A-4A71-8604-2A797BE359CA",
//                categoryID = null,
//                iP = GettingIP(this@MapActivityOld).deviceIpAddress,
//                type = typeApi
//            )
//        ).observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<MapResponse> {
//                override fun onSuccess(dataInput: MapResponse) {
//                    progressBar.dismiss()
//                    Log.i("testMap", "onCreate: ${dataInput.isSuccess}")
//                    if (dataInput.isSuccess == true) {
//                        if (!dataInput.data.isNullOrEmpty()) {
//                            moveCamera(
//                                LatLng(
//                                    dataInput.data?.get(0)?.latitude ?: 51.213890,
//                                    dataInput.data?.get(0)?.longitude ?: -102.462776
//                                )
//                            )
//                            list = dataInput.data
//                            initPagerPoster()
//
//                            adapter.update(list = dataInput.data ?: arrayListOf())
//                            showPinInMap()
//
//                        }
//
//                    }
//                }
//
//                override fun onException(exception: Exception) {
//                    progressBar.dismiss()
//
//                }
//
//            })
//        )
//    }
//
//    fun showPinInMap() {
//        list?.forEachIndexed { index, testTO ->
//            Log.i("testMap", "onCreate: ${testTO.title}")
//            val marketOption = MarkerOptions().position(LatLng(testTO.latitude, testTO.longitude))
//
//
//            val markerMap = googleMap?.addMarker(marketOption)
//            markerMap?.tag = index
//            listMarker?.add(markerMap!!)
//
//            changeIconMarker(markerMap, false)
//
//
//        }
//    }
//
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
//    }
//
//    private fun initPagerPoster() {
//
//        binding.viewPagerPoster.offscreenPageLimit = 3;
//        binding.viewPagerPoster.adapter = adapter;
//        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
//        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
//        binding.viewPagerPoster.setPageTransformer { page, position ->
//            val myOffset = position * -(2 * pageOffset + pageMargin)
//            if (binding.viewPagerPoster.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
//                if (ViewCompat.getLayoutDirection(binding.viewPagerPoster) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                    page.translationX = -myOffset;
//                } else {
//                    page.translationX = myOffset;
//                }
//            } else {
//                page.translationY = myOffset;
//            }
//        }
//
//        viewPagerScale()
//
//    }
//
//    fun viewPagerScale() {
//
//        binding.viewPagerPoster.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//
//                gotoLatLng_Trades(
//                    LatLng(
//                        list?.get(position)?.latitude ?: 51.213890,
//                        list?.get(position)?.longitude ?: -102.462776
//                    ), position
//                )
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//            }
//        })
//
//
//    }
//
//
//    private fun gotoLatLng_Trades(latLng: LatLng, position: Int) {
//        /** change icon before , after : marketOld itemi hast k qablan choose shode va alan bayad  halae disSelected shavad **/
//        if (markerOld != null)
//            changeIconMarker(marker = markerOld, false)
//        markerOld = listMarker?.get(position)
//
//
//        changeIconMarker(listMarker?.get(position), true)
//        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f), 2000, null)
//    }
//
//    private fun changeIconMarker(marker: Marker?, selected: Boolean) {
//        when (typeApi) {
//            ContextApp.RENT -> {
//                if (selected)
//                    marker?.setIcon(
//                        bitmapDescriptorFromVector(
//                            this@MapActivityOld,
//                            R.drawable.ic_map_rent_selected
//                        )
//                    )
//                else
//                    marker?.setIcon(
//                        bitmapDescriptorFromVector(
//                            this@MapActivityOld,
//                            R.drawable.ic_map_rent
//                        )
//                    )
//            }
//            ContextApp.SALE -> {
//                if (selected)
//                    marker?.setIcon(
//                        bitmapDescriptorFromVector(
//                            this@MapActivityOld,
//                            R.drawable.ic_map_sale_selected
//                        )
//                    )
//                else
//                    marker?.setIcon(
//                        bitmapDescriptorFromVector(
//                            this@MapActivityOld,
//                            R.drawable.ic_map_sale
//                        )
//                    )
//            }
//
//        }
//    }
//
//    fun componentSearch() {
//        val mList = ArrayList<LatLngPlace>();
//        mList.add(LatLngPlace("Toronto", LatLng(43.651070, -79.347015)))
//        mList.add(LatLngPlace("Tehran", LatLng(35.715298, 51.404343)))
//
//        adapterSection = AdapterSearchPlace(
//            this,
//            R.layout.text_list,
//            R.id.text,
//            mList
//        )
//
//        binding.tAutoCompeleteNecessry.setAdapter(adapterSection)
//        binding.tAutoCompeleteNecessry.setText(mList[0].cityName, false)
//
//        binding.tAutoCompeleteNecessry.onItemClickListener =
//            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
//                Toast.makeText(this@MapActivityOld, mList[position].cityName, Toast.LENGTH_SHORT)
//                    .show()
//                moveCamera(mList[position].latlng)
//            }
//
//        binding.componentSearch.setStartIconOnClickListener {
//            onClickChooseCity()
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//
//        this.googleMap = googleMap
//        googleMap.setMaxZoomPreference(16f)
//        this.googleMap?.setOnMarkerClickListener(this)
////        this.googleMap?.isMyLocationEnabled = true
//
//
//        markets(typeApi = typeApi)
//
//    }
//
//    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
//        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
//        vectorDrawable!!.setBounds(
//            0,
//            0,
//            vectorDrawable.intrinsicWidth,
//            vectorDrawable.intrinsicHeight
//        )
//        val bitmap = Bitmap.createBitmap(
//            vectorDrawable.intrinsicWidth,
//            vectorDrawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        vectorDrawable.draw(canvas)
//        return BitmapDescriptorFactory.fromBitmap(bitmap)
//    }
//
//    fun moveCamera(latLng: LatLng?) {
//        googleMap?.setMaxZoomPreference(16f)
//        val cameraPosition = CameraPosition.Builder()
//            .target(
//                latLng ?: LatLng(43.651070, -79.347015)
//            ) // Sets the center of the map to Mountain View
//            .zoom(10f)
//            .build() // Creates a CameraPosition from the builder
//
//        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        googleMap?.moveCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                latLng ?: LatLng(
//                    43.651070,
//                    -79.347015
//                ), 15f
//            )
//        )
//    }
//
//
//    fun getCenter(view: View) {
//        val latLng = googleMap?.cameraPosition?.target
//
////        checkCountry(latLng);
//        val intent = Intent()
//        intent.putExtra(ContextApp.LATITUDE, latLng?.latitude.toString() + "")
//        intent.putExtra(ContextApp.LONGITUDE, latLng?.longitude.toString() + "")
//        setResult(RESULT_OK, intent)
//        finish()
//    }
//
//    override fun onMapClick(p0: LatLng) {
//
//    }
//
//    /* OnClick Sale */
//    fun onClickSale(view: View) {
//        binding.cardSale.setCardBackgroundColor(
//            ContextCompat.getColor(
//                this@MapActivityOld,
//                R.color.colorPrimary
//            )
//        )
//        binding.imgSale.setColorFilter(ContextCompat.getColor(this@MapActivityOld, R.color.white))
//
//        binding.cardRent.setCardBackgroundColor(
//            ContextCompat.getColor(
//                this@MapActivityOld,
//                R.color.white
//            )
//        )
//        binding.imgRent.setColorFilter(ContextCompat.getColor(this@MapActivityOld, R.color.tinticon))
//
//
//        markerOld = null;
//        listMarker?.clear()
//        googleMap?.clear()
//        typeApi = ContextApp.SALE
//        markets(typeApi = ContextApp.SALE)
//    }
//
//    /* Onclick Rent */
//    fun onClickRent(view: View) {
//        binding.cardSale.setCardBackgroundColor(
//            ContextCompat.getColor(
//                this@MapActivityOld,
//                R.color.white
//            )
//        )
//        binding.imgSale.setColorFilter(ContextCompat.getColor(this@MapActivityOld, R.color.tinticon))
//
//        binding.cardRent.setCardBackgroundColor(
//            ContextCompat.getColor(
//                this@MapActivityOld,
//                R.color.colorPrimary
//            )
//        )
//        binding.imgRent.setColorFilter(ContextCompat.getColor(this@MapActivityOld, R.color.white))
//
//        markerOld = null;
//        listMarker?.clear()
//        googleMap?.clear()
//        typeApi = ContextApp.RENT
//        markets(typeApi = ContextApp.RENT)
//    }
//    /* Onclick Service */
//    fun onClickService(view: View) {
//
//        if (googleMap?.mapType == GoogleMap.MAP_TYPE_NORMAL)
//            googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
//        else
//            googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
//
//    }
//
//    /* Onclick TypeMap */
//    fun onClickTypeMap(view: View) {
//
//        if (googleMap?.mapType == GoogleMap.MAP_TYPE_NORMAL)
//            googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
//        else
//            googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
//
//    }
//
//    fun onClickChooseCity(){
//        val showAreaDialog = ShowAreaDialog()
//        showAreaDialog.onReturnPlace = this
//        showAreaDialog.show(supportFragmentManager, "area")
//    }
//
//    override fun onReturnPlace(placeTO: PlaceTO) {
//        binding.tAutoCompeleteNecessry.setText(placeTO.name.toString())
//        city = placeTO.name
//        cityID = placeTO.id
//
//        markerOld = null
//        listMarker?.clear()
//        googleMap?.clear()
//        markets(typeApi = typeApi , cityID = placeTO.id?:"")
//    }
//
//    override fun onMarkerClick(marker: Marker): Boolean {
//        if (marker.tag != null) {
//            binding.viewPagerPoster.currentItem = marker.tag.toString().toInt()
//        }
//
//        /** change icon before , after : marketOld itemi hast k qablan choose shode va alan bayad  halae disSelected shavad **/
//        if (markerOld != null)
//            changeIconMarker(marker = markerOld, false)
//        markerOld = marker
//
//        changeIconMarker(marker, true)
//
//        return false
//    }
//
//    override fun onClickAdapter(mapData: MapData?, action: String, binding: AdapterMapBinding) {
//        when (action) {
//            ContextApp.DETAILS -> {
//                val pedpo = Intent(this@MapActivityOld, DetailsActivity::class.java);
//                pedpo.putExtra(ContextApp.MARKET_ID, mapData?.marketID)
//                pedpo.putExtra(ContextApp.TYPE_API, mapData?.isService)
//
//                startActivity(pedpo);
//
//            }
//
//            ContextApp.BOOKMARK -> {
//                addBookmark(mapData = mapData, binding = binding);
//            }
//            ContextApp.LIKE -> {
//                like(mapData = mapData, binding = binding);
//            }
//        }
//    }
//
//
//    fun addBookmark(mapData: MapData?, binding: AdapterMapBinding) {
//
//        viewModelDetails.addBookmark(
//            marketID = mapData?.marketID,
//            if (mapData?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
//        )?.observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
//                override fun onSuccess(dataInput: BookmarkData) {
//
//
//                    if (dataInput.isSuccess == true) {
//                        MyMutable.mutableBookmark.postValue(MyMutable.BooleanBookmark(bookmark = true))
//                        mapData?.isBookMarkByUser = dataInput.data?.isBookMarkByUser == true;
//
//                        if (dataInput.data?.isBookMarkByUser!!)
//                            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked);
//                        else
//                            binding.icBookmark.setImageResource(R.drawable.ic_bookmark);
//                    }
//
//                }
//
//                override fun onException(exception: Exception) {
//
//                    //                    Log.e("testbookmark", "onException: " + exception.message)
//                }
//
//            })
//        )
//    }
//
//    fun like(mapData: MapData?, binding: AdapterMapBinding) {
//
//        viewModelDetails.like(
//            viewTO = ViewTO(
//                mapData?.marketID ?: "",
//                GettingIP(this@MapActivityOld).deviceIpAddress ?: "",
//                if (mapData?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
//            )
//        )?.observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
//                override fun onSuccess(dataInput: LikeData) {
//
//
//                    mapData?.isLikeByIp = dataInput.data?.isLikeByIP == true;
//
//
//                    if (dataInput.data?.isLikeByIP!!) {
//                        binding.icLike.setImageResource(R.drawable.ic_liked)
//                    } else {
//                        binding.icLike.setImageResource(R.drawable.ic_like)
//                    }
//
//                }
//
//                override fun onException(exception: Exception) {
//                    Log.e("testView", "onSuccess: ${exception.message}")
//                }
//            })
//        )
//    }
//
//
//}