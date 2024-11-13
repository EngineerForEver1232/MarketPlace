package com.pedpo.pedporent.view.paging.seeMoreRecommended.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.databinding.PagingActivityBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.paging.seeMoreRecommended.adapter.SMRecomMarketPagingAdapter
import com.pedpo.pedporent.view.paging.seeMoreRecommended.viewModel.SMRecomViewModel
import com.pedpo.pedporent.viewModel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SeeMRecommendedMarketActivity : AppCompatActivity(), ClickAdapterPaging {

    private var adapter: SMRecomMarketPagingAdapter? = null;

    private val viewModel: SMRecomViewModel by viewModels();

    private val viewModelDetails: DetailsViewModel by viewModels();

    @Inject
    lateinit var showProgressBar: ShowProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PagingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tTitle.text = getString(R.string.the_recommended_ads)

        binding.recycler.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false);
        adapter = SMRecomMarketPagingAdapter(applicationContext);
        adapter?.clickAdapterPaging = this;


        viewModel?.pagedListSMRecom?.observe(this, { pagedList ->
            adapter!!.submitList(pagedList) }
        )

        viewModel?.networkState?.observe(this, { networkState: NetworkState? ->
            adapter!!.setNetworkState(networkState!!)
            binding?.swipeRefreshLayout?.isRefreshing = networkState == NetworkState.LOADING
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel?.invalidateDataSource()
        }

        binding.recycler.adapter = adapter

    }


    override fun onClickAdapterPaging(
        paginTO: PaginTO?,
        action: String,
        binding: AdapterPaginBinding
    ) {
        when (action) {
            ContextApp.DETAILS -> {
                var intent = Intent(this, DetailsActivity::class.java);
                intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
                startActivity(intent);
            }
            ContextApp.BOOKMARK -> {
                addBookmark(paginTO, binding = binding)
            }
            ContextApp.LIKE -> {
                like(paginTO, binding = binding)
            }
        }
    }

    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginBinding) {
        showProgressBar.show(supportFragmentManager)
        viewModelDetails?.addBookmark(marketID = paginTO?.marketID,paginTO?.type)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess!!) {
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

        viewModelDetails?.like(
            viewTO = ViewTO(
                paginTO?.marketID?:"",
                GettingIP(this).deviceIpAddress?:"",
                paginTO?.type?:""
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {

                    if (dataInput.data?.isLikeByIP!!) {
                        binding.icLike.setImageResource(R.drawable.ic_liked)
                    } else {
                        binding.icLike.setImageResource(R.drawable.ic_like)
                    }
//                    Toast.makeText(
//                        this@SeeMRecommendedMarketActivity,
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