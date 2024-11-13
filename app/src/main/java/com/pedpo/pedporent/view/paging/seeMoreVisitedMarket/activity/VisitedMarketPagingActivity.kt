package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.adapter.VisitedMarketPaginAdapter
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.viewModel.VisitedMarketViewModel
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VisitedMarketPagingActivity : AppCompatActivity(), ClickAdapterPaging {

    private var adapterVisitedMarket: VisitedMarketPaginAdapter? = null;

    private lateinit var binding: PagingActivityBinding;
    private val viewModelVisitedMarket: VisitedMarketViewModel by viewModels();
    private val viewModelDetails: DetailsViewModel by viewModels();

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var prefApp: PrefApp;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PagingActivityBinding.inflate(layoutInflater);
        setContentView(binding.root)
        binding.tTitle.text = getString(R.string.the_most_visited_ads)

        /**
         * Step 2: Setup the adapter class for the RecyclerView
         * */

        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapterVisitedMarket = VisitedMarketPaginAdapter(applicationContext);
        adapterVisitedMarket?.clickAdapterPaging = this;


        /**
         * Step 4: When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         * */

        viewModelVisitedMarket?.filterTextAll?.value =
            RequestSearchMarket(title = "", categoryID = "", prefApp.getCityID(), null,GettingIP(this).deviceIpAddress!!);
//        viewModel?.marketLiveData?.observe(this

        viewModelVisitedMarket?.pagedList?.observe(
            this
        ) { pagedList ->
            run {
                adapterVisitedMarket!!.submitList(pagedList)
            }
        }

        /**
         * Step 5: When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         * */
        viewModelVisitedMarket?.networkState?.observe(this) { networkState: NetworkState? ->
            run {
                adapterVisitedMarket!!.setNetworkState(networkState!!)
                binding.swipeRefreshLayout.isRefreshing =
                    networkState == NetworkState.LOADING;
            }
        }

        binding.recycler.adapter = adapterVisitedMarket


        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModelVisitedMarket.invalidateDataSource()
        }


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
                Toast.makeText(this, "bookmark", Toast.LENGTH_SHORT).show();
            }
            ContextApp.LIKE -> {
                Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
                like(paginTO, binding = binding)
            }
        }
    }

    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginBinding) {
        showProgressBar.show(supportFragmentManager)
        viewModelDetails?.addBookmark(marketID = paginTO?.marketID, type = paginTO?.type)?.observe(
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
                    Log.e("testbookmark", "onException: " + exception.message)
                }

            })
        )
    }


    fun like(paginTO: PaginTO?, binding: AdapterPaginBinding) {

        viewModelDetails?.like(
            viewTO = ViewTO(
                marketID = paginTO?.marketID?:"",
                GettingIP(this).deviceIpAddress?:"",
                type = paginTO?.type?:""
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
                    Toast.makeText(
                        this@VisitedMarketPagingActivity,
                        dataInput.data?.isLikeByIP.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("testView", "onSuccess: ${dataInput.message}")
                }

                override fun onException(exception: Exception) {
                    Log.e("testView", "onSuccess: ${exception.message}")
                }
            })
        )
    }


}