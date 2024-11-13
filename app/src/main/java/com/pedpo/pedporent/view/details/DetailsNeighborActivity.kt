package com.pedpo.pedporent.view.details

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager2.widget.ViewPager2
import com.pedpo.pedporent.R
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailTO
import com.pedpo.pedporent.view.adapter.AlbumAdapter
import com.pedpo.pedporent.view.adapter.CommentAdapter
import com.pedpo.pedporent.view.adapter.DepositDetailsAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.dialog.progress.ShowLoadingPedpo
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.pedpo.pedporent.databinding.ActivityNeighborDetailsBinding
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.viewModel.DetailsNeighborViewModel
import com.squareup.picasso.Picasso
import kotlin.math.abs

@AndroidEntryPoint
class DetailsNeighborActivity : AppCompatActivity() {


    lateinit var binding: ActivityNeighborDetailsBinding

    @Inject
    lateinit var commentAdapter: CommentAdapter;

    @Inject
    lateinit var albumAdapter: AlbumAdapter;

    @Inject
    lateinit var depositAdapter: DepositDetailsAdapter;

    @Inject
    lateinit var showLoadingPedpo: ShowLoadingPedpo;

    @Inject
    lateinit var showProgressBar: ShowProgressBar;

    @Inject
    lateinit var numberFormatDigits: NumberFormatDigits

    private val viewModel: DetailsNeighborViewModel by viewModels();

    private var marketID: String? = null;
    private var urlLink: String? = null;



    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeighborDetailsBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root);
        setToolbar()
        showLoadingPedpo.show(fragmentManager = supportFragmentManager);


        marketID = intent.getStringExtra(ContextApp.MARKET_ID)?:"";


//        initAlbum()

        animActionBar()

        initDetails()

    }

    fun setToolbar(){
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId==android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }



    var userID: String? = null;

    fun initDetails() {
        Log.e("detailsNeighbor", ": ${marketID}" )

        viewModel.detailMarkets(
            viewTO = ViewTO(
                neighborMarketID =  if (marketID.isNullOrEmpty()) 0 else marketID?.toInt(),
                marketID = "",
                iP =   GettingIP(this).deviceIpAddress!!,
                type = ""
            )
        ).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<DetailsData> {
                override fun onSuccess(dataInput: DetailsData) {
//                    Log.e("detailsNeighbor", ": ${dataInput?.isSuccess}" )
//                    Log.e("detailsNeighbor", ": ${dataInput?.data?.urlLink}" )
//                    Log.e("detailsNeighbor", ": ${dataInput?.data?.neighborImage}" )
//                    Log.e("detailsNeighbor", ": ${dataInput?.data?.description}" )
//                    Log.e("detailsNeighbor", "commodityPrice : ${dataInput?.data?.commodityPrice}" )
//                    Log.e("detailsNeighbor", "rentPrice : ${dataInput?.data?.rentPrice}" )

                    if (dataInput?.isSuccess!!) {
                        if (dataInput?.data ==null)
                            return

                        urlLink = dataInput?.data?.urlLink;

                        binding.viewModel = dataInput.data;
                        var details = dataInput.data;

                        binding.nestedScroll.isVisible = true;
                        showLoadingPedpo.dismiss();

                        userID = details?.renterUserID;

                        Picasso.get().load(details?.neighborImage).placeholder(R.drawable.placeholder).into(binding?.img)

                        binding.tDescription.text = dataInput.data?.description;


                        if (dataInput?.data?.rentPrice!=null)
                        binding?.tPriceT.text = numberFormatDigits?.convertToDigist(
                            dataInput?.data?.rentPrice?:0)


                        binding.linearDropDown.isVisible = binding.tDescription.lineCount > ContextApp.COUNT_LINE;



                    }
                }

                override fun onException(exception: Exception) {
                    showLoadingPedpo.dismiss();
                    Log.e("detailsNeighbor", "onException: ${exception.localizedMessage}" )
                }

            })
        )
    }


    private var checkAnim = false

    fun animActionBar() {
        binding.appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            val offsetFactor = (-verticalOffset).toFloat() / scrollRange.toFloat()
            Log.e("scroll", "onOffsetChanged: $offsetFactor")
            colorAction(offsetFactor)
            colorActionTextViewToolbar(offsetFactor)
            colorActionImageView(offsetFactor)
//            if (verticalOffset == 0) binding.tTitleToolbar.visibility = View.GONE
//            if (verticalOffset <= -(scrollRange - binding.toolbar.height / 2)) {
//                if (checkAnim) {
//                    binding.tTitleToolbar.visibility = View.VISIBLE
//                    val animationShow = AnimationUtils.loadAnimation(
//                        this@DetailsActivity,
//                        R.anim.anim_show_alpha
//                    )
//                    binding.tTitleToolbar.startAnimation(animationShow)
//                    checkAnim = false
//                }
//            } else {
//                if (!checkAnim) {
//                    val animationHide = AnimationUtils.loadAnimation(
//                        this@DetailsActivity,
//                        R.anim.anim_hiden_alpha
//                    )
//                    binding.tTitleToolbar.startAnimation(animationHide)
//                    animationHide.setAnimationListener(object : Animation.AnimationListener {
//                        override fun onAnimationStart(animation: Animation) {}
//                        override fun onAnimationEnd(animation: Animation) {
//                            binding.tTitleToolbar.visibility = View.GONE
//                        }
//
//                        override fun onAnimationRepeat(animation: Animation) {}
//                    })
//                    checkAnim = true
//                }
//            }
        })
    }

    fun colorAction(offsetFactor: Float) {
        var i = 0.30f
        var j = 0.70f
        i = if (offsetFactor <= 0.30f) {
            0.30f
        } else offsetFactor

        j = if (offsetFactor <= 0.70f) {
            0.70f
        } else offsetFactor

        Log.i("offsetFactor", "offsetFactor: $i")

        val drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                Color.argb(
                    (j * 255).toInt(),
//                    (255).toInt(),
                    230,
                    230,
                    230
                ),
                Color.argb(
                    (i * 255).toInt(),
//                    ( 255).toInt(),
                    230,
                    230,
                    230
                ),
                Color.argb(
                    (offsetFactor * 255).toInt(),
                    230,
                    230,
                    230
                )

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
        binding.tTitleToolbar.setTextColor(
            Color.argb(
                (offsetFactor * 255).toInt(),
                0,
                250,
                250
            )
        )
    }


    fun colorActionImageView(offsetFactor: Float) {
        binding.icBack.setBackgroundResource(R.drawable.circle_gray)
        val gd = binding.icBack.background.current;
        gd?.setColorFilter(
            Color.argb(
                (abs(offsetFactor-1) * 255).toInt(),
                100,
                100,
                100
            )
            , PorterDuff.Mode.SRC_IN)
//        gd?.colorFilter = BlendModeColorFilter(
//            Color.argb(
//                (abs(offsetFactor-1) * 255).toInt(),
//                100,
//                100,
//                100
//            )
//            , BlendMode.SRC_IN)

        binding?.icBack.setColorFilter(
            Color.argb(
                (255).toInt(),
                (abs(offsetFactor-1)  * 255).toInt(),
                (abs(offsetFactor-1)  * 255).toInt(),
                (abs(offsetFactor-1)  * 255).toInt()
            )
        )


        binding?.tTitleToolbar.setTextColor(
            Color.argb(
                ( ((if (offsetFactor >= 0.7) offsetFactor else 0.0f))  * 255).toInt(),
                0,
                0,
                0
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

    private var listPhotos: List<PhotoDetailTO>? = null;


    var registerOnPage =
        object : ViewPager2.OnPageChangeCallback() {
        }

    /*OnClick*/
    fun onClickContact(view: View) {

        var intent = Intent(this@DetailsNeighborActivity,DetailsWebView::class.java);
        intent.putExtra(ContextApp.LINK,urlLink)
        startActivity(intent)

    }
    /*OnClick*/
    fun onClickIcBack(view: View) {
        finish()
    }

    fun animConstraintDetails() {
        val constraintSet = ConstraintSet()
//        constraintSet.load(this, R.layout.activity_language_country)
        TransitionManager.beginDelayedTransition(binding.constraintDes)
        constraintSet.applyTo(binding?.constraintDes)
    }

    /*onClick Read More*/
    fun onClickReadMore(view: View) {
        if (binding.tDescription.maxLines == ContextApp.COUNT_LINE){
            binding.tDescription.maxLines = Integer.MAX_VALUE;
            binding?.linearDropDown.isVisible=false
            animConstraintDetails()
        }
        else if (binding.tDescription.maxLines == Integer.MAX_VALUE) {
            binding.tDescription.maxLines = ContextApp.COUNT_LINE;
            binding?.linearDropDown.isVisible=true
            animConstraintDetails()

        }
    }

}