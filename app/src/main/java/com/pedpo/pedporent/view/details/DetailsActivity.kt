package com.pedpo.pedporent.view.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityDetailsBinding
import com.pedpo.pedporent.listener.IOnClickDialog
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.comment.AddComment
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailTO
import com.pedpo.pedporent.model.details.PhotoDetailsData
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.AlbumAdapter
import com.pedpo.pedporent.view.adapter.CommentAdapter
import com.pedpo.pedporent.view.adapter.DepositDetailsAdapter
import com.pedpo.pedporent.view.dialog.ContactDetailsFragDialog
import com.pedpo.pedporent.view.dialog.DoRegister
import com.pedpo.pedporent.view.dialog.FragmentDialogAlbumImage
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.dialog.progress.ShowLoadingPedpo
import com.pedpo.pedporent.view.profile.ProfileActivityUser
import com.pedpo.pedporent.view.store.detailStore.StoreDetailsActivity
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.widget.dotsIndicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity(), IOnClickDialog, OnMapReadyCallback,
    GoogleMap.OnMapClickListener {

    private var shareLink: String?=null;
    lateinit var binding: ActivityDetailsBinding
    @Inject
    lateinit var commentAdapter: CommentAdapter;
    @Inject
    lateinit var numberFormatDigits: NumberFormatDigits;
    @Inject
    lateinit var albumAdapter: AlbumAdapter;
    @Inject
    lateinit var depositAdapter: DepositDetailsAdapter;
    @Inject
    lateinit var showLoadingPedpo: ShowLoadingPedpo;
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var utillsApp: UtillsApp;
    private val viewModel: DetailsViewModel by viewModels();
    private var googleMap: GoogleMap? = null
    private var marketID: String? = null
    private var typeAPI: String? = null
    private var userID: String? = null
    private var storeID: String? = null
    private var lat = ""
    private var lng = ""
    private var listPhotos: List<PhotoDetailTO>? = null;


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        binding.listener = this
        setContentView(binding.root)
        showLoadingPedpo.show(fragmentManager = supportFragmentManager)


        typeAPI = if (intent.getBooleanExtra(ContextApp.TYPE_API, false))
            ContextApp.SERVICE else ContextApp.MARKET

        val data: Uri? = intent?.data

//        Toast.makeText(this@DetailsActivity,data?.query,Toast.LENGTH_SHORT).show()
        if (!intent.getStringExtra(ContextApp.MARKET_ID).isNullOrEmpty()) {
            marketID = intent.getStringExtra(ContextApp.MARKET_ID) ?: ""
        } else {
            if (intent.data == null)
                return
            marketID = intent.data?.getQueryParameter("id")
            if (marketID.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.does_not_exists),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        getStatusComment(marketID = marketID)

        initRecyclerDeposit()

        initAlbum()
        animActionBar()

        initDetails()

        initComment()

        showMap()


        binding.tSend.setOnClickListener {
            sendComment(binding.eComment.text.toString().trim())
        }

        binding.icBookmark.setOnClickListener {
            addBookmark()
        }
        binding.icLike.setOnClickListener {
            like()
        }

        binding.tMap.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$lat,$lng")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }
        binding.tStreetView.setOnClickListener {
            if (lat.isNotEmpty() && lng.isNotEmpty()) {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=$lat,$lng")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
        viewMarkets()
    }

    fun showMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    fun initRecyclerDeposit() {
        binding.recyclerDepositt.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerDepositt.adapter = depositAdapter;
    }

    fun like() {
        viewModel.like(
            viewTO = ViewTO(
                marketID ?: "",
                GettingIP(this).deviceIpAddress ?: "",
                type = typeAPI ?: ""
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {

                    if (dataInput.data?.isLikeByIP!!) {
                        binding.icLike.setImageResource(R.drawable.ic_liked)
                        binding.tCountLike.setText(
                            (binding.tCountLike.text.toString().toInt() + 1).toString()
                        );
                    } else {
                        binding.icLike.setImageResource(R.drawable.ic_like)
                        if (binding.tCountLike.text.toString().toInt() > 0)
                            binding.tCountLike.setText(
                                (binding.tCountLike.text.toString().toInt() - 1).toString()
                            );
                    }

                    Log.i("testView", "onSuccess: ${dataInput.message}")
                }

                override fun onException(exception: Exception) {
                    Log.e("testView", "onSuccess: ${exception.message}")
                }
            })
        )
    }

    fun viewMarkets() {

        viewModel.view(
            viewTO = ViewTO(
                marketID = marketID ?: "",
                iP = GettingIP(this).deviceIpAddress ?: "",
                type = typeAPI ?: ""
            )
        ).observe(
                this,
                CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                    override fun onSuccess(dataInput: ResponseTO) {

                    }

                    override fun onException(exception: Exception) {

                    }
                })
            )
    }

    fun addBookmark() {

        viewModel.addBookmark(marketID = marketID, type = typeAPI ?: ContextApp.MARKET)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    MyMutable.mutableBookmark.postValue(
                        MyMutable.BooleanBookmark(
                            bookmark = true,
                            home = true
                        )
                    )

                    showLoadingPedpo.dismiss();
                    if (dataInput.isSuccess!!) {
                        if (dataInput.data?.isBookMarkByUser!!)
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked)
                        else
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmark)
                    }
                }

                override fun onException(exception: Exception) {
                }
            })
        )
    }




    fun initDetails() {

        viewModel.detailMarkets(
            viewTO = ViewTO(
                marketID = marketID ?: "",
                GettingIP(this).deviceIpAddress ?: "",
                type = typeAPI ?: ContextApp.MARKET
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<DetailsData> {
                override fun onSuccess(dataInput: DetailsData) {
                    if (dataInput.isSuccess!!) {

                        if (dataInput.data == null)
                            return
                        binding.viewModel = dataInput.data
                        val details = dataInput.data

                        binding.nestedScroll.isVisible = true
                        showLoadingPedpo.dismiss()

                        userID = details?.renterUserID
                        storeID = details?.storeID


                        shareLink = details?.shareLink
                        Log.e("testDetails", "onOffsetChanged: ${details?.shareLink}")
//                        Log.e("testDetails", "onOffsetChanged: ${details?.type}")
//                        Log.e("testDetails", "onOffsetChanged: ${details?.rentPrice}")
//                        Log.e("testDetails", "onOffsetChanged: ${details?.commodityPrice}")
//                        Log.e("testDetails", "onOffsetChanged: ${details?.salePrice}")
//                        Log.e("testDetails", "onOffsetChanged: ${details?.priceAgree}")
//                        Log.e("testDetails", "onOffsetChanged: ${details?.free}")

                        if (details?.startTimeInactive!=null && details.endTimeInactive!=null){
                            val startDate = utillsApp.startDate(details.startTimeInactive?:"")
                            val endDate = utillsApp.endDate( details.endTimeInactive?:"")

                            binding.tStartDate.text = startDate.get(Calendar.DAY_OF_MONTH).toString()
                            binding.tEndDate.text = endDate.get(Calendar.DAY_OF_MONTH).toString()

                            binding.startMonth.text = startDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA)
                            binding.endMonth.text = endDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA)

                        }


                        if (prefApp.getToken().isEmpty()) {
                            binding.icBookmark.isVisible = false
                            binding.linearAddComment.isVisible = false
                        }

                        if (details?.storeID.isNullOrEmpty())
                            binding.lableViewProfile.text = getString(R.string.view_profile)
                            else
                            binding.lableViewProfile.text = getString(R.string.view_store)

                        binding.tPriceType.text = details?.type
                        when {
                            details?.free == true -> {
                                binding.tPriceT.text = getString(R.string.free)
                                binding.linearPrice.isVisible = false
                            }
                            details?.priceAgree == true -> {
                                binding.tPriceT.text = getString(R.string.an_agreement)
                                binding.linearPrice.isVisible = false
                            }
                            else -> {
                                binding.linearPrice.isVisible = true


                                when (details?.type) {
                                    ContextApp.SALE -> {
                                        binding.tPriceT.text = numberFormatDigits.convertToDigist(
                                            details.salePrice ?: 0
                                        )
                                    }
                                    ContextApp.RENT -> {
                                        if (details.showType == ContextApp.M){
                                            binding.tPriceT.text = numberFormatDigits.convertToDigist(details.monthlyRentalPrice?:0L)
                                            binding.typePrice.text = getString(R.string.monthly)
                                        }else if (details.showType == ContextApp.D){
                                            binding.tPriceT.text = numberFormatDigits.convertToDigist(details.rentPrice?:0L)
                                            binding.typePrice.text = getString(R.string.daily)
                                        }else if (details.showType == ContextApp.H){
                                            binding.tPriceT.text = numberFormatDigits.convertToDigist(details.hourlyRentalPrice?:0L)
                                            binding.typePrice.text = getString(R.string.hourly)
                                        }else{
                                            binding.tPriceT.text = numberFormatDigits.convertToDigist(details.yearlyRentPrice?:0L)
                                            binding.typePrice.text = getString(R.string.annual)
                                        }

                                    }
                                    else -> {
                                        binding.tPriceT.text = numberFormatDigits.convertToDigist(
                                            details?.rentPrice ?: 0
                                        )
                                    }
                                }
                            }
                        }
                        if (details?.type == ContextApp.SALE || details?.type == ContextApp.SERVICE || details?.type == null) {
                            binding.linearPrice.isVisible = false
                            binding.constraintDeposit.isVisible = false

                        }
                        if (
                            details?.typeOfguarantee?.check == false &&
                            details.typeOfguarantee?.identityCard == false &&
                            details.typeOfguarantee?.nationalCard == false &&
                            details.typeOfguarantee?.promissoryNote == false &&
                            details.typeOfguarantee?.other == false
                        ) {
                            binding.constraintDeposit.isVisible = false;
                        }


                        val list = ArrayList<String>();

                        if (details?.typeOfguarantee?.check == true)
                            list.add(getString(R.string.check))
                        if (details?.typeOfguarantee?.identityCard == true)
                            list.add(getString(R.string.certificate))
                        if (details?.typeOfguarantee?.nationalCard == true)
                            list.add(getString(R.string.national_id_card))
                        if (details?.typeOfguarantee?.promissoryNote == true)
                            list.add(getString(R.string.promissory_note))
                        if (details?.typeOfguarantee?.other == true)
                            list.add(details?.typeOfguarantee?.otherText.toString())


                        if (details?.latitude != null && details?.longitude != null) {
                            moveCameraMap(
                                details.latitude?.toDouble(),
                                details.longitude?.toDouble()
                            )
                            lat = details.latitude ?: "";
                            lng = details.longitude ?: "";
                        } else {
                            binding.carViewMap.isVisible = false;
                        }

                        binding.tDescription.text = dataInput.data?.description;

                        binding.linearDropDown.isVisible =
                            binding.tDescription.lineCount > ContextApp.COUNT_LINE;

                        depositAdapter.updateAdapter(list)

                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    fun animConstraintDetails() {
        val constraintSet = ConstraintSet()
        TransitionManager.beginDelayedTransition(binding.constraintDes)
        constraintSet.applyTo(binding.constraintDes)
    }

    /*onClick Read More*/
    fun onClickReadMore(view: View) {
        if (binding.tDescription.maxLines == ContextApp.COUNT_LINE) {
            binding.tDescription.maxLines = Integer.MAX_VALUE
            binding.linearDropDown.isVisible = false
            animConstraintDetails()
        } else if (binding.tDescription.maxLines == Integer.MAX_VALUE) {
            binding.tDescription.maxLines = ContextApp.COUNT_LINE;
            binding.linearDropDown.isVisible = true
            animConstraintDetails()
        }
    }

    /*onClick Profile*/
    fun onClickLayoutProfile(view: View) {
        val intent :Intent = if (storeID.isNullOrEmpty())
            Intent(view.context, ProfileActivityUser::class.java).putExtra(ContextApp.USER_ID, userID)
        else
            Intent(view.context, StoreDetailsActivity::class.java).putExtra(ContextApp.STORE_ID, storeID)

        startActivity(intent)
    }
    /*onClick Share*/
    fun onClickShare(view: View) {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
        shareIntent.type = "text/plain";
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        shareIntent.setType("image/jpeg");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    fun animActionBar() {
        binding.appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            val offsetFactor = (-verticalOffset).toFloat() / scrollRange.toFloat()
            Log.e("scroll", "onOffsetChanged: $offsetFactor")
            colorAction(offsetFactor)
            colorActionTextViewToolbar(offsetFactor)
            colorActionImageView(offsetFactor)
        })
    }

//    fun colorAction(offsetFactor: Float) {
//        var i = 0.00f
//        var j = 0.00f
//        i = if (offsetFactor <= 0.00f) {
//            0.00f
//        } else offsetFactor
//
//        j = if (offsetFactor <= 0.00f) {
//            0.00f
//        } else offsetFactor
//
//        Log.i("offsetFactor", "offsetFactor: $i")
//
//        val drawable = GradientDrawable(
//            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
//                Color.argb(
//                    (j * 255).toInt(),
////                    (255).toInt(),
//                    230,
//                    230,
//                    230
//                ),
//                Color.argb(
//                    (i * 255).toInt(),
////                    ( 255).toInt(),
//                    230,
//                    230,
//                    230
//                ),
//                Color.argb(
//                    (offsetFactor * 255).toInt(),
//                    230,
//                    230,
//                    230
//                )
//
//            )
//        )
//
//        binding.toolbar.background = drawable;
////        binding.toolbar.setBackgroundColor(
////            Color.argb(
////                (i * 255).toInt(),
////                230,
////                230,
////                230
////            )
////        )
//    }

    fun colorAction(offsetFactor: Float) {
        var i = 0.00f
        var j = 0.00f
        i = if (offsetFactor <= 0.00f) {
            0.00f
        } else offsetFactor

        j = if (offsetFactor <= 0.00f) {
            0.00f
        } else offsetFactor

        Log.i("offsetFactor", "offsetFactor: $i")

        val drawable = ColorDrawable(
                Color.argb(
                    (j * 255).toInt(),
//                    (255).toInt(),
                    getString(R.integer.color_appbar_detial).toInt(),
                    getString(R.integer.color_appbar_detial).toInt(),
                    getString(R.integer.color_appbar_detial).toInt()
                )
        )

        binding.toolbar.background = drawable;
//        binding.toolbar.setBackgroundColor(
//            Color.argb(
//                (i * 255).toInt(),
//                230,
//                230,
//                230
//            )
//        )
    }

    fun colorActionTextViewToolbar(offsetFactor: Float) {
//        binding.tTitleToolbar.setTextColor(
//            Color.argb(
//                (offsetFactor * 255).toInt(),
//                0,
//                250,
//                250
//            )
//        )
    }


    fun colorActionImageView(offsetFactor: Float) {
        binding.icBack.setBackgroundResource(R.drawable.circle_gray)
        val gd = binding.icBack.background.current;
        gd.setColorFilter(
            Color.argb(
                (abs(offsetFactor - 1) * 255).toInt(),
                getString(R.integer.color_appbar_background_back).toInt(),
                getString(R.integer.color_appbar_background_back).toInt(),
                getString(R.integer.color_appbar_background_back).toInt()
                ), PorterDuff.Mode.SRC_IN
        )
//        gd?.colorFilter = BlendModeColorFilter(
//            Color.argb(
//                (abs(offsetFactor-1) * 255).toInt(),
//                100,
//                100,
//                100
//            )
//            , BlendMode.SRC_IN)

        binding.icBack.setColorFilter(
            Color.argb(
                (255).toInt(),
                (abs(offsetFactor -
                        getString(R.integer.positive).toInt()
                ) * getString(R.integer.color_appbar_ic_back).toInt()).toInt(),
                (abs(offsetFactor - getString(R.integer.positive).toInt()) * getString(R.integer.color_appbar_ic_back).toInt()).toInt(),
                (abs(offsetFactor - getString(R.integer.positive).toInt()) * getString(R.integer.color_appbar_ic_back).toInt()).toInt()
            )
        )


        binding.tTitleToolbar.setTextColor(
            Color.argb(
                (((if (offsetFactor >= 0.7) offsetFactor else 0.0f)) * 255).toInt(),
                getString(R.integer.black_dark_integer).toInt(),
                getString(R.integer.black_dark_integer).toInt(),
                getString(R.integer.black_dark_integer).toInt()
            )
        )

//        val unwrappedDrawable = AppCompatResources.getDrawable(this@DetailsActivity, R.drawable.circle_gray)
//        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//        DrawableCompat.setTint(wrappedDrawable,
//         ContextCompat.getColor(this@DetailsActivity,R.color.red_error)
//        )
//
//        Color.argb(
//            (offsetFactor * 255).toInt(),
//            0,
//            238,
//            238
//        )

//        binding.icBack.background = wrappedDrawable

    }


    private fun initAlbum() {
        albumAdapter.iOnClickDialog = this

        binding.viewpagerAlbum.adapter = albumAdapter

        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        dotsIndicator.setViewPager2(binding.viewpagerAlbum)
        binding.viewpagerAlbum.registerOnPageChangeCallback(registerOnPage)

        viewModel.detailPhotos(
            marketID ?: "",
            typeAPI = typeAPI ?: ContextApp.MARKET
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<PhotoDetailsData> {
                override fun onSuccess(dataInput: PhotoDetailsData) {
                    if (dataInput.isSuccess) {
                        binding.appbar.isVisible = true
                        albumAdapter.updateAdapter(dataInput.data!!)
                        listPhotos = dataInput.data
                    } else {
                        Toast.makeText(this@DetailsActivity, dataInput.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    var registerOnPage =
        object : ViewPager2.OnPageChangeCallback() {
        }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpagerAlbum.unregisterOnPageChangeCallback(registerOnPage)
    }

    fun sendComment(comment: String) {
        if (comment.isEmpty())
            return;

        val addComment = AddComment(marketID!!, comment, typeAPI ?: "")
        viewModel.sendComment(addComment = addComment)
            .observe(this, CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    binding.eComment.text?.clear()
                    viewModel.refreshComment(marketID = marketID!!, typeAPI = typeAPI)
                    insertComment()
                }

                override fun onException(exception: Exception) {
                    Toast.makeText(this@DetailsActivity, exception.message, Toast.LENGTH_SHORT)
                        .show();
                }

            }))
    }

    private fun insertComment() {


        viewModel.comments(marketID = marketID ?: "", typeAPI = typeAPI).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<CommentData> {
                override fun onSuccess(dataInput: CommentData) {

                    commentAdapter.insertAdapter(dataInput.data!!)
                    Log.i("testComment", "onSuccess: " + dataInput.message)
                }

                override fun onException(exception: Exception) {
                    Log.e("testComment", "onException: " + exception.message)
                }
            })
        )
    }


    private fun initComment() {
        binding.recyclerComment.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerComment.adapter = commentAdapter

        viewModel.comments(marketID = marketID ?: "", typeAPI = typeAPI).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<CommentData> {
                override fun onSuccess(dataInput: CommentData) {

                    commentAdapter.adapter(dataInput.data)
                    Log.i("testComment", "onSuccess: " + dataInput.message)
                }

                override fun onException(exception: Exception) {
                    Log.e("testComment", "onException: " + exception.message)
                }
            })
        )
    }

    /*OnClick*/
    fun onClickIcBack(view: View) {
        finish()
    }

    @Inject
    lateinit var prefApp: PrefApp

    /*OnClick*/
    fun onClickContact(view: View) {
        if (prefApp.getToken().isEmpty()) {
            DoRegister(this@DetailsActivity).show().observe(this) {
                Toast.makeText(this@DetailsActivity, it.toString(), Toast.LENGTH_SHORT).show()
            }
            return
        }


        viewModel.checkRenter(userID =
        if (!userID.isNullOrEmpty()) userID?:"" else storeID?:""
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<RenterData> {
                override fun onSuccess(dataInput: RenterData) {
                    Log.i("testUserID", "response $storeID")
                    Log.i("testUserID", "response ${dataInput.data?.phoneNumber}")
                    Log.i("testUserID", "response ${dataInput.data?.email}")
                    Log.i("testUserID", "response ${dataInput.data?.name}")
                    Log.i("testUserID", "response ${dataInput.data?.image}")
                    if (dataInput.isSuccess) {
                        val showPhone =
                            ContactDetailsFragDialog().newInstance(dataInput.data, true)
                        showPhone.show(supportFragmentManager, "showPhone")
                    } else {
                        val showPhone = ContactDetailsFragDialog().newInstance(null, false)
                        showPhone.show(supportFragmentManager, "showPhone")
                    }
                }

                override fun onException(exception: Exception) {
                    Log.e("testUserID", "response ${exception.message}")
                }
            })
        )
    }

    /*OnClick @AdapterAlbum*/
    override fun onClickDialog() {
        val fragmentDialogAlbumImage =
            FragmentDialogAlbumImage().newInstance(
                binding.viewpagerAlbum.currentItem,
                listPhotos as ArrayList<PhotoDetailTO>?
            )
        fragmentDialogAlbumImage.setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
        fragmentDialogAlbumImage.show(supportFragmentManager, "fragment")

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap?.setOnMapClickListener(this)
    }

    fun moveCameraMap(lat: Double?, lng: Double?) {
        val latLng = LatLng(lat ?: 29.4963, lng ?: 60.8629)

        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to Mountain View
            .zoom(10f)
            .build() // Creates a CameraPosition from the builder

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))


        val marketOption = MarkerOptions()
        marketOption.position(latLng)

        googleMap?.addMarker(marketOption)

    }

    private fun getStatusComment(marketID:String?){
        viewModel.getStatusComment(marketID = marketID?:"")
            ?.observe(this@DetailsActivity,
                CustomObserver(object :CustomObserver.ResultListener<ResponseComment>{
                    override fun onSuccess(dataInput: ResponseComment) {

                        binding.linearAddComment.isVisible = !dataInput.isCloseComment
                    }

                    override fun onException(exception: Exception) {

                    }
                }))
    }


    override fun onMapClick(latlng: LatLng) {


    }

}