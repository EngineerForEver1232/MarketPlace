package com.pedpo.pedporent.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityProfileTowBinding
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.model.renterMarket.RenterMarketData
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.LastedAdapter
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.details.DetailsNeighborActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivityUser : AppCompatActivity() , ClickAdapterPaging {

    lateinit var binding: ActivityProfileTowBinding;
    @Inject
    lateinit var lastedAdapter: LastedAdapter;
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private val viewModelDetails: DetailsViewModel by viewModels();
    var userID:String?=null;

    private val viewModel: ProfileViewModel by viewModels();

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileTowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar();

        userID = intent.getStringExtra(ContextApp.USER_ID)?:""

        initRenterMarket()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun initRenterMarket() {
        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = lastedAdapter
        lastedAdapter.clickAdapterPaging = this

        viewModel.renterMarkets(userID = userID ?:"",
            GettingIP(this@ProfileActivityUser).deviceIpAddress)?.observe(this,
            CustomObserver(object :CustomObserver.ResultListener<RenterMarketData>{
                override fun onSuccess(dataInput: RenterMarketData) {
                    if (dataInput.isSuccess==true)
                    lastedAdapter.updateAdapter(dataInput.data?.listRenterMarketsViewModel)

                    binding.tName.text = dataInput.data?.firstName?:""
                    if (!dataInput.data?.image.isNullOrEmpty())
                    Picasso.get().load(dataInput.data?.image).placeholder(R.drawable.ic_profile).into(binding.imgProfile)

                }

                override fun onException(exception: Exception) {

                }
            }))
    }

    override fun onClickAdapterPaging(
        paginTO: PaginTO?,
        action: String,
        binding: AdapterPaginBinding
    ) {
        when (action) {
            ContextApp.DETAILS -> {

                if (paginTO?.nameSite.isNullOrEmpty() || paginTO?.nameSite == "Pedpo"){
                    val pedpo = Intent(this@ProfileActivityUser, DetailsActivity::class.java)
                    pedpo.putExtra(ContextApp.MARKET_ID, paginTO?.marketID)
                    startActivity(pedpo)
                }else{
                    val intent = Intent(this@ProfileActivityUser, DetailsNeighborActivity::class.java)
                    intent.putExtra(ContextApp.MARKET_ID, paginTO?.neighborMarketID.toString())
                    startActivity(intent)
                }
            }

            ContextApp.BOOKMARK -> {
                if (paginTO?.nameSite.isNullOrEmpty()) {
                    addBookmark(paginTO = paginTO, binding = binding)
                }
            }
            ContextApp.LIKE -> {
                like(paginTO = paginTO, binding = binding)
            }
        }
    }


    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginBinding) {
        showProgressBar.show(supportFragmentManager)
        viewModelDetails.addBookmark(marketID = paginTO?.marketID,
            type = if (paginTO?.isService==true) ContextApp.SERVICE else ContextApp.MARKET

        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess!!) {
                        paginTO?.isBookMarkByUser = dataInput.data?.isBookMarkByUser

                        if (dataInput.data?.isBookMarkByUser!!)
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked)
                        else
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmark)
                    }

                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
//                    Log.e("testbookmark", "onException: " + exception.message)
                }

            })
        )
    }


    fun like(paginTO: PaginTO?, binding: AdapterPaginBinding) {
        viewModelDetails.like(
            viewTO = ViewTO(
                paginTO?.marketID?:"",
                GettingIP(this@ProfileActivityUser).deviceIpAddress,
                type = if (paginTO?.isService==true) ContextApp.SERVICE else ContextApp.MARKET
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {
                    paginTO?.isLikeByIp = dataInput.data?.isLikeByIP


                    if (dataInput.data?.isLikeByIP!!) {
                        binding.icLike.setImageResource(R.drawable.ic_liked)
                    } else {
                        binding.icLike.setImageResource(R.drawable.ic_like)
                    }
//                    Toast.makeText(
//                        this@LastMarketActivity,
//                        dataInput.data?.isLikeByIP.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Log.i("testView", "onSuccess: ${dataInput.message}")
                }

                override fun onException(exception: Exception) {
//                    Log.e("testView", "onSuccess: ${exception.message}")
                }
            })
        )
    }


}