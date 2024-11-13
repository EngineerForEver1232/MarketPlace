package com.pedpo.pedporent.view.paging.search.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.databinding.ActivitySearchCategoryBinding
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.listener.ClickAdapterRecentlySearch
import com.pedpo.pedporent.listener.ClickAdpterSearchCategory
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.model.search.SearchCategoryData
import com.pedpo.pedporent.model.search.SearchCategoryTO
import com.pedpo.pedporent.model.search.market.RequestSearchMarket
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState
import com.pedpo.pedporent.room.dao.RecentSearchDAO
import com.pedpo.pedporent.room.entity.RecentSearch
import com.pedpo.pedporent.utills.ContextApp

import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.RecentSearchAdapter
import com.pedpo.pedporent.view.adapter.SearchCategoryAdapter
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.paging.search.adapter.SearchMarketPagingAdapter
import com.pedpo.pedporent.view.paging.search.viewModel.SearchMarketPagingViewModel
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList
import androidx.core.view.isVisible

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), ClickAdpterSearchCategory , ClickAdapterRecentlySearch ,
    ClickAdapterPaging {

    private var adapterPaging: SearchMarketPagingAdapter? = null;
    @Inject
    lateinit var recentSearchAdapter: RecentSearchAdapter;
    @Inject
    lateinit var searchCategoryAdapter: SearchCategoryAdapter;
    @Inject
    lateinit var recentSearchDAO: RecentSearchDAO;
    @Inject
    lateinit var serviceAPI: ServiceAPI;
    @Inject
    lateinit var prefApp: PrefApp;

    private val searchViewModel: SearchViewModel by viewModels();
    private val viewModelPaging: SearchMarketPagingViewModel by viewModels();
    private val viewModelDetails: DetailsViewModel by viewModels();

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private var binding: ActivitySearchCategoryBinding? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCategoryBinding.inflate(layoutInflater);
        binding?.listener = this ;
        setContentView(binding?.root) ;


        binding?.recyclerRecentSearch?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding?.recyclerRecentSearch?.adapter = recentSearchAdapter ;
        recentSearchAdapter.clickAdapterRecentlySearch = this ;


        showRecentlySearch();
        Log.i("recentSearch", "showRecentlySearch: ")
        Toast.makeText(this@SearchActivity , "Fsafs",Toast.LENGTH_SHORT).show();

        searchCategory();

        binding?.recyclerSearch?.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false);
        adapterPaging = SearchMarketPagingAdapter(this);
        adapterPaging?.clickAdapterPaging = this;
        binding?.recyclerSearch?.adapter = adapterPaging;


        viewModelPaging.pagedList?.observe(this
        ) { pagedList ->
            Log.e("testNetwork", "pagedList: doing")
            adapterPaging?.submitList(pagedList)
        }

        viewModelPaging.filterTextAll.value =
            RequestSearchMarket(title = "", categoryID = "", prefApp.getCityID(), null);

        viewModelPaging.netWorkState?.observe(this) { networkState: NetworkState? ->
            Log.e("testNetwork", "netWorkState: doing");
            adapterPaging?.setNetworkState(networkState!!);
            binding?.swipeRefreshLayout?.isRefreshing = networkState == NetworkState.LOADING;
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            binding?.eSearch?.setText("");
            viewModelPaging.filterTextAll?.value =
                RequestSearchMarket(title = "", categoryID = "", prefApp.getCityID(), null);
            viewModelPaging.invalidateDataSource();
        }


    }

    fun searchCategory() {
        binding?.recyclerSearchCategory?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding?.recyclerSearchCategory?.adapter = searchCategoryAdapter;
        searchCategoryAdapter.clickAdpterSearchCategory = this ;

        binding?.eSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                binding?.icDelete?.isVisible = text.isNullOrEmpty();
                binding?.recyclerRecentSearch?.isVisible = text.isNullOrEmpty();
                binding?.constraintTitle?.isVisible = text.isNullOrEmpty();
                binding?.recyclerSearchCategory?.isVisible = !text.isNullOrEmpty();
                binding?.recyclerSearch?.isVisible = false;

                showCategory(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun showCategory(title: String) {
        searchViewModel.searchCategory(title = title).observe(this,
            CustomObserver(object : CustomObserver.ResultListener<SearchCategoryData> {
                override fun onSuccess(dataInput: SearchCategoryData) {

                    if (dataInput?.isSuccess == true)
                        searchCategoryAdapter.updateAdapter(dataInput.data)
                }

                override fun onException(exception: Exception) {

                }
            }))
    }


    /*onClick adapter */
    override fun onClickAdapterSearchCategory(searchCategory: SearchCategoryTO) {

        binding?.recyclerSearch?.isVisible = true;

        binding?.recyclerSearchCategory?.visibility = View.GONE;
        viewModelPaging.filterTextAll.value =
            RequestSearchMarket(title = binding?.eSearch?.text?.trim().toString(),
                categoryID = searchCategory.categoryID,
                prefApp.getCityID(),
                null);
//        viewModelPaging.param(serviceAPI,searchCategory.title,searchCategory.categoryID)
//        viewModelPaging.init()
        viewModelPaging.invalidateDataSource()


        CoroutineScope(Dispatchers.IO).launch {
            var response = recentSearchDAO.insert(
                RecentSearch(
                    text =  searchCategory.title?:"",
                    nameCategory = searchCategory.categotyName?:"",
                    idCategory = searchCategory.categoryID?:""

                ));
            withContext(Dispatchers.Main){
                showRecentlySearch()
            }
        }

    }

    fun showRecentlySearch(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = recentSearchDAO.select();
            withContext(Dispatchers.Main){
                Log.i("recentSearch", "showRecentlySearch: ${response?.size}")
                recentSearchAdapter.updateAdapter(response)
            }
        }
    }

    /*onClick Back*/
    fun onClickBack(view: View){
        finish()
    }
    /*onClick Delete All*/
    fun onClickDeleteAll(view: View){

        CoroutineScope(Dispatchers.IO).launch {
            var response = recentSearchDAO.deleteTable();
            withContext(Dispatchers.Main){
                recentSearchAdapter.updateAdapter(ArrayList<RecentSearch>())
            }
        }
    }

    /*onClick Delete Adapter*/
    override fun onClickRecentlySearch(recentSearch: RecentSearch?,type:String?) {
        if (type == ContextApp.DELETE) {
            CoroutineScope(Dispatchers.IO).launch {
                var response = recentSearchDAO.delete(recentSearch = recentSearch!!);
                withContext(Dispatchers.Main) {
                    showRecentlySearch()
                }
            }
        }else {

            binding?.icDelete?.isVisible = false;
            binding?.recyclerRecentSearch?.isVisible = false;
            binding?.constraintTitle?.isVisible = true;
            binding?.recyclerSearchCategory?.isVisible = false;
            binding?.recyclerSearch?.isVisible = true;

            binding?.recyclerSearchCategory?.visibility = View.GONE;
            viewModelPaging?.filterTextAll?.value =
                RequestSearchMarket(title = recentSearch?.text,
                    categoryID = recentSearch?.idCategory,
                    prefApp.getCityID(),
                    null);
//        viewModelPaging.param(serviceAPI,searchCategory.title,searchCategory.categoryID)
//        viewModelPaging.init()
            viewModelPaging?.invalidateDataSource()
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
            }
            ContextApp.LIKE -> {
                like(paginTO, binding = binding)
            }
        }
    }

    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginBinding) {
        showProgressBar.show(supportFragmentManager)
        viewModelDetails.addBookmark(marketID = paginTO?.marketID, type = paginTO?.type)?.observe(
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


    fun like(paginTO:PaginTO?, binding: AdapterPaginBinding) {

        viewModelDetails.like(
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
//                        this@SearchActivity,
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
