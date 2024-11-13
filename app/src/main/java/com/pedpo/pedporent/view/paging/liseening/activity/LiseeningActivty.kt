package com.pedpo.pedporent.view.paging.liseening.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.databinding.PagingActivityBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.details.DetailsNeighborActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.paging.liseening.adapter.LiseenPagingAdapter
import com.pedpo.pedporent.view.paging.liseening.model.TransferData
import com.pedpo.pedporent.view.paging.liseening.viewmodel.LiseenPagingViewModel
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.LastedPagingAdapter
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel.LastMarketViewModel2
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.viewModel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class LiseeningActivty : AppCompatActivity(), ClickAdapterPaging {

    private val viewModel: LiseenPagingViewModel by viewModels();
    lateinit var binding: PagingActivityBinding
//    private var adapter: LiseenPagingAdapter? = null;
    private var adapter: LastedPagingAdapter? = null;

    var id = "";

    @Inject
    lateinit var prefApp: PrefApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PagingActivityBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding?.tTitle.text = getString(R.string.listenning)

        id = intent?.getStringExtra(ContextApp?.MARKET_ID)?:""

        pagingLastedMarket()
    }

    fun pagingLastedMarket() {
        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = LastedPagingAdapter(prefApp = prefApp,this);
        adapter?.clickAdapterPaging = this;

        binding.recycler.adapter = adapter?.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter?.retry()
            }
        )


        lifecycleScope.launch {
            viewModel.lastMarket(
                TransferData(
                    id = id,
                    ip = GettingIP(this@LiseeningActivty).deviceIpAddress
                )
            )?.collectLatest {
                adapter?.submitData(it)
            }
        }


        lifecycleScope.launch {
            adapter?.loadStateFlow?.collect { loadState ->

                val refreshState = loadState.refresh;

//                Log.i("testRefresh", " else : ${loadState?.refresh} ${refreshState is LoadState.Error}" )
                binding.recycler.isVisible = refreshState !is LoadState.Error
//                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
//                binding?.included?.layoutError.isVisible = refreshState is LoadState.Error


                if (loadState?.refresh is LoadState.Loading) {
//                    Log.i("testRefresh", " if : ${loadState?.refresh}")
//                    Log.i("testRefresh", "loading size : ${adapter?.snapshot()?.isEmpty()} ")


                    if ((adapter?.itemCount ?: 0) <= 0) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
//                        binding?.included?.layoutError?.isVisible = true;
                        binding?.labelError?.text = getString(R.string.no_items)
                        binding.recycler.isVisible = false;
                    } else {
                        binding?.layoutError?.isVisible = false
                        binding?.recycler?.isVisible = true
                    }
                } else {
//                    Log.i("testRefresh", "notLoading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if ((adapter?.itemCount ?: 0) <= 0) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
                        binding?.layoutError?.isVisible = true
                        binding?.labelError?.text = getString(R.string.no_items)
                        binding?.recycler?.isVisible = false
                    } else {
                        binding?.layoutError?.isVisible = false
                        binding?.recycler?.isVisible = true
                    }


                    if (refreshState is LoadState.Error) {
                        when (refreshState.error as Exception) {
                            is HttpException ->
                                binding?.labelError.text = getString(R.string.internal_error)
                            is IOException ->
                                binding.labelError.text = getString(R.string.label_no_internet)
                        }

                        val errorState = loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error

                        errorState?.let {
                            Toast.makeText(
                                this@LiseeningActivty,
                                ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        }

        binding?.reloadPostsBtn.setOnClickListener {
            adapter?.refresh();
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
//            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
            binding.swipeRefreshLayout.isRefreshing = false;
            adapter?.refresh()
        }
    }

    override fun onClickAdapterPaging(
        paginTO: PaginTO?,
        action: String,
        binding: AdapterPaginBinding
    ) {
        when (action) {
            ContextApp.DETAILS -> {

                if (paginTO?.nameSite.isNullOrEmpty() || paginTO?.nameSite == "Pedpo") {
                    var pedpo = Intent(this, DetailsActivity::class.java);
                    pedpo.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
                    pedpo.putExtra(ContextApp.TYPE_API, paginTO?.isService)

                    startActivity(pedpo);
                } else {
                    var intent = Intent(this, DetailsNeighborActivity::class.java);
                    intent.putExtra(ContextApp.MARKET_ID, paginTO?.neighborMarketID.toString());
                    startActivity(intent);
                }
            }

            ContextApp.BOOKMARK -> {
                if (paginTO?.nameSite.isNullOrEmpty() || paginTO?.nameSite == "Pedpo") {
                    addBookmark(paginTO = paginTO, binding = binding);
                }
            }
            ContextApp.LIKE -> {
                if (paginTO?.nameSite.isNullOrEmpty()) {
                }
                like(paginTO = paginTO, binding = binding);
            }
        }
    }

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private val viewModelDetails: DetailsViewModel by viewModels();

    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginBinding) {

        showProgressBar.show(supportFragmentManager)
        viewModelDetails?.addBookmark(
            marketID = paginTO?.marketID,
            if (paginTO?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    Log.i(
                        "testBookmark", "onSuccess: ${paginTO?.type} ${dataInput?.isSuccess}" +
                                " ${dataInput?.data?.isBookMarkByUser} ${dataInput?.message} ${paginTO?.marketID}"
                    )
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {
                        paginTO?.isBookMarkByUser = dataInput.data?.isBookMarkByUser;

                        if (dataInput.data?.isBookMarkByUser!!)
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked);
                        else
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmark);
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
                paginTO?.marketID ?: "",
                GettingIP(this@LiseeningActivty).deviceIpAddress ?: "",
                if (paginTO?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {

                    Log.i(
                        "testLike", "onSuccess: ${paginTO?.type} ${dataInput?.isSuccess} " +
                                "${dataInput?.data?.isLikeByIP} ${dataInput?.message}"
                    )

                    paginTO?.isLikeByIp = dataInput.data?.isLikeByIP;


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