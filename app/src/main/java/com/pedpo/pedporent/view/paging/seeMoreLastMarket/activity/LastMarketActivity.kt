package com.pedpo.pedporent.view.paging.seeMoreLastMarket.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.nav.CounterPage
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.LastedPagingAdapter
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel.LastMarketViewModel
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.viewModel.DetailsViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel.LastMarketViewModel2
import kotlinx.coroutines.Job

@AndroidEntryPoint
class LastMarketActivity : AppCompatActivity() , ClickAdapterPaging {

    private var adapter: LastedPagingAdapter? = null;

//    private val viewModel: LastMarketViewModel by viewModels();

    @Inject
    lateinit var prefApp: PrefApp
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private val viewModelDetails: DetailsViewModel by viewModels();
    private var searchJob: Job? = null
    private val viewModel: LastMarketViewModel2 by viewModels();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PagingActivityBinding.inflate(layoutInflater);
        setContentView(binding.root);

        adapterPagin(binding = binding)
        pagingLastedMarket("Rent")

    }

    fun adapterPagin(binding:PagingActivityBinding){

        binding.recycler.apply {
            layoutManager =LinearLayoutManager(this@LastMarketActivity, LinearLayoutManager.VERTICAL, false);
            setHasFixedSize(true)
        }

        adapter = LastedPagingAdapter(prefApp = prefApp,this);
        adapter?.clickAdapterPaging = this;

        binding.recycler.adapter = adapter?.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter?.retry()
            }
        )

        adapter?.addLoadStateListener { loadState ->

            Log.i("testRefresh", " : ${loadState.refresh}")
            val refreshState = loadState.refresh;


            if (loadState?.refresh is LoadState.Loading) {


                if (adapter?.snapshot()?.isEmpty() == true) {
//                    binding.progress.isVisible = true
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                binding.layoutError.isVisible = false

            } else {
//                binding.progress.isVisible = false
                Log.i("testRefresh", "error : ${loadState.refresh}")
                binding.swipeRefreshLayout.isRefreshing = false

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0;

                // Only show the list if refresh succeeds.
                binding.recycler.isVisible = !isListEmpty

                binding.recycler.isVisible = refreshState is LoadState.NotLoading
                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
                binding.layoutError.isVisible = refreshState is LoadState.Error

                if (refreshState is LoadState.Error){
                    when(refreshState.error as Exception){
                        is HttpException ->
                            binding.labelError.text = getString(R.string.internal_error)
                        is IOException ->
                            binding.labelError.text = getString(R.string.label_no_internet)
                    }

                    val errorState = loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error

                    errorState?.let {
                        Toast.makeText(
                            this@LastMarketActivity,
                            ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }



//        lifecycleScope.launch {
//            adapter?.loadStateFlow?.collect { loadState ->
//
//                val refreshState = loadState.refresh;
//                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter?.itemCount == 0;
//
//                // Only show the list if refresh succeeds.
//                binding.recycler.isVisible = !isListEmpty
//
//                binding.recycler.isVisible = refreshState is LoadState.NotLoading
//                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
//                binding.layoutError.isVisible = refreshState is LoadState.Error
//
//                if (refreshState is LoadState.Error){
//                    when(refreshState.error as Exception){
//                        is HttpException ->
//                            binding.labelError.text = getString(R.string.internal_error)
//                        is IOException ->
//                            binding.labelError.text = getString(R.string.label_no_internet)
//                    }
//
//                    val errorState = loadState.append as? LoadState.Error
//                        ?: loadState.prepend as? LoadState.Error
//
//                    errorState?.let {
//                        Toast.makeText(
//                            this@LastMarketActivity,
//                            ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        }

        binding.reloadPostsBtn.setOnClickListener {
            adapter?.refresh();
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
//            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
            binding.swipeRefreshLayout.isRefreshing = false;
            pagingLastedMarket(type = typeApi)
        }
    }
    }

    var typeApi = "Rent";

    fun pagingLastedMarket(type:String){

        if (type == "Rent")
            typeApi = "Sale"
        else if (type=="Sale")
            typeApi = "Rent"


        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.lastMarket(
                null,
                GettingIP(this@LastMarketActivity).deviceIpAddress,prefApp,typeApi
            )?.collectLatest {
                Log.e("testBack1", "adapter launch : " + CounterPage.pages.size)
                adapter?.submitData(it)
            }
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
                addBookmark(paginTO, binding = binding);
            }
            ContextApp.LIKE -> {
                like(paginTO, binding = binding);
            }
        }
    }


    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginBinding) {
        showProgressBar.show(supportFragmentManager)
        viewModelDetails?.addBookmark(marketID = paginTO?.marketID,type = paginTO?.type)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess!!) {
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