package com.pedpo.pedporent.view.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivitySearchFilterBinding
import com.pedpo.pedporent.databinding.AdapterPaginFilterBinding
import com.pedpo.pedporent.listener.ClickAdapter
import com.pedpo.pedporent.listener.ClickAdapterPagingFilter
import com.pedpo.pedporent.listener.ClickAdapterRecentlySearch
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.model.filter.FilterTransfer
import com.pedpo.pedporent.room.dao.RecentSearchDAO
import com.pedpo.pedporent.room.entity.RecentSearch
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.OrderAdapter
import com.pedpo.pedporent.view.adapter.RecentSearchAdapter
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.details.DetailsNeighborActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.paging.filter.adapter.FilterAdapter
import com.pedpo.pedporent.view.paging.filter.viewModel.FilterMarketViewModel
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.viewModel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class SearchFilterActivity : AppCompatActivity(), ClickAdapterPagingFilter, ClickAdapter,
    ClickAdapterRecentlySearch {

    var adapter: FilterAdapter? = null;
    private val viewModel: FilterMarketViewModel by viewModels()

    @Inject
    lateinit var prefApp: PrefApp
    var tSearch: String? = null;
    lateinit var binding: ActivitySearchFilterBinding

    private var filterTransfer: FilterTransfer? = null

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private val viewModelDetails: DetailsViewModel by viewModels();

    @Inject
    lateinit var orderAdapter: OrderAdapter;

    @Inject
    lateinit var recentSearchAdapter: RecentSearchAdapter;

    @Inject
    lateinit var recentSearchDAO: RecentSearchDAO;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        binding.listener = this
        setContentView(binding.root)
        setToolbar(binding)
        filterTransfer = FilterTransfer()

        searchCategory(binding)

        binding.recyclerRecentSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerRecentSearch.adapter = recentSearchAdapter
        recentSearchAdapter.clickAdapterRecentlySearch = this

        binding.recyclerOrder.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerOrder.adapter = orderAdapter
        orderAdapter.updateAdapter(resources.getStringArray(R.array.order))
        orderAdapter.returnContent = this


        adapter = FilterAdapter(this)
        adapter?.clickAdapterPaging = this
        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter
        adapter?.clickAdapterPaging = this


        binding.recycler.adapter = adapter?.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter?.retry()
            }
        )


        binding.inputSearch.setEndIconOnClickListener {
            binding.eSearch.setText("");
            binding.cardRecentSearch.isVisible = false
        }

        binding.eSearch.setOnFocusChangeListener { view, b ->
            Log.i("3636", "setOnFocusChangeListener: ${recentSearchAdapter.itemCount}")

            if (recentSearchAdapter.itemCount > 0)
                binding.cardRecentSearch.isVisible = true
        }
        binding.eSearch.setOnClickListener {

            Log.i("3636", "setOnClickListener: ${recentSearchAdapter.itemCount}")
//            if (recentSearchAdapter.itemCount > 0)
                binding.cardRecentSearch.isVisible = true

        }


        binding.eSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.eSearch.text.toString().isNotEmpty())
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = recentSearchDAO.insert(
                            RecentSearch(
                                text = binding.eSearch.text.toString(),
                                nameCategory = "",
                                idCategory = ""
                            )
                        );
                        withContext(Dispatchers.Main) {
                            showRecentlySearch()
                        }
                    }
                true
            } else false
        })

        viewModel.initData(FilterTransfer())

//         filterTransfer = FilterTransfer(
//            title = "",
//            cityID = city,
//            categoryID = null,
//            type = "Sale",
//            priceFrom = 200000,
//            priceTo = 500000,
//            priceAgree = false,
//            free = false,
//            registerDate = null,
//            iP = GettingIP(this).deviceIpAddress?:"ip"
//        );


        lifecycleScope.launch {
            adapter?.loadStateFlow?.collect { loadState ->

                val refreshState = loadState.refresh;

                Log.i(
                    "testRefresh",
                    " else : ${loadState?.refresh} ${refreshState is LoadState.Error}"
                )

//                binding.recycler.isVisible = refreshState !is LoadState.Error
//                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
//                binding.layoutError.isVisible = refreshState is LoadState.Error


                if (loadState?.refresh is LoadState.Loading) {
                    Log.i("testRefresh", " if : ${loadState?.refresh}")
                    Log.i("testRefresh", "loading size : ${adapter?.snapshot()?.isEmpty()} ")


                    if (adapter?.snapshot()?.isEmpty() == true) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
                        binding.included?.layoutError.isVisible = true;
                        binding.included?.labelError.text = getString(R.string.no_items)
                        binding.recycler.isVisible = false;
                    } else {
                        binding.included?.layoutError.isVisible = false
                        binding.recycler.isVisible = true
                    }
                } else {
                    Log.i("testRefresh", "notLoading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if (adapter?.snapshot()?.isEmpty() == true) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
                        binding.included?.layoutError.isVisible = true
                        binding.included?.labelError.text = getString(R.string.no_items)
                        binding.recycler.isVisible = false
                    } else {
                        binding.included?.layoutError.isVisible = false
                        binding.recycler.isVisible = true
                    }


                    if (refreshState is LoadState.Error) {
                        when (refreshState.error as Exception) {
                            is HttpException ->
                                binding.included?.labelError.text =
                                    getString(R.string.internal_error)
                            is IOException ->
                                binding.included?.labelError.text =
                                    getString(R.string.label_no_internet)
                        }

                        val errorState = loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error

                        errorState?.let {
                            Toast.makeText(
                                this@SearchFilterActivity,
                                ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        }

        binding.included.reloadPostsBtn.setOnClickListener {
            adapter?.refresh();
        }

        showRecentlySearch()
//        binding.swipeRefreshLayout.setOnRefreshListener {
////            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
//            binding.swipeRefreshLayout.isRefreshing = false;
//            adapter?.refresh()
//        }


    }

    fun showRecentlySearch() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = recentSearchDAO.select()
            withContext(Dispatchers.Main) {
                Log.i("3636", "onClickAdapterPaging: ${response?.size}")
                recentSearchAdapter.updateAdapter(response)
            }
        }
    }

    /*onClick Delete Adapter*/
    override fun onClickRecentlySearch(recentSearch: RecentSearch?, type: String?) {
        if (type == ContextApp.DELETE) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = recentSearchDAO.delete(recentSearch = recentSearch!!)
                Log.i("3636", "onClickRecentlySearch: ${response}")
                withContext(Dispatchers.Main) {
                    showRecentlySearch()
                }
            }
        } else {

            binding.cardRecentSearch.isVisible = false
            binding.constraintTitle.isVisible = true
            binding.recycler.isVisible = true


            tSearch = recentSearch?.text
            filterTransfer?.title = recentSearch?.text


            viewModel.initData(filterTransfer!!)
            lifecycleScope.launch {
                viewModel.posts.collectLatest {
//                        Log.e("testFilter", " launch : " + CounterPage.pages.size)
                    adapter?.submitData(it)
                }
            }
            adapter?.refresh()

        }
    }


    /*onClick Delete All*/
    fun onClickDeleteAll(view: View) {

        CoroutineScope(Dispatchers.IO).launch {
            var response = recentSearchDAO.deleteTable();
            withContext(Dispatchers.Main) {
                recentSearchAdapter.updateAdapter(ArrayList<RecentSearch>())
            }
        }
    }

    fun setToolbar(binding: ActivitySearchFilterBinding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    fun onclickOrder(view: View) {
        binding.recyclerOrder.isVisible = !binding.recyclerOrder.isVisible
    }

    fun searchCategory(binding: ActivitySearchFilterBinding) {

        binding.eSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                tSearch = text.toString()
                filterTransfer?.title = text.toString()

                if (filterTransfer?.cityID.isNullOrEmpty())
                    filterTransfer?.cityID = prefApp.getCityID()
//                binding.icDelete?.isVisible = text.isNullOrEmpty();

                viewModel.initData(filterTransfer!!)
                lifecycleScope.launch {
                    viewModel.posts.collectLatest {
//                        Log.e("testFilter", " launch : " + CounterPage.pages.size)
                        adapter?.submitData(it)
                    }
                }
                adapter?.refresh()

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }


    override fun onClickAdapterPaging(
        paginTO: PaginTO?,
        action: String,
        binding: AdapterPaginFilterBinding
    ) {
        when (action) {
            ContextApp.DETAILS -> {

                if (paginTO?.nameSite.isNullOrEmpty() || paginTO?.nameSite == "Pedpo") {
                    val pedpo = Intent(this@SearchFilterActivity, DetailsActivity::class.java);
                    pedpo.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
                    pedpo.putExtra(ContextApp.TYPE_API, paginTO?.isService);

                    startActivity(pedpo);
                } else {
                    val intent =
                        Intent(this@SearchFilterActivity, DetailsNeighborActivity::class.java);
                    intent.putExtra(ContextApp.MARKET_ID, paginTO?.neighborMarketID.toString());
                    startActivity(intent);
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val response = recentSearchDAO.insert(
                        RecentSearch(
                            text = paginTO?.title ?: "",
                            nameCategory = "",
                            idCategory = ""

                        )
                    );
                    withContext(Dispatchers.Main) {
                        showRecentlySearch()
                    }
                }

            }

            ContextApp.BOOKMARK -> {
                if (paginTO?.nameSite.isNullOrEmpty()) {
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


    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginFilterBinding) {
        showProgressBar.show(supportFragmentManager)
        viewModelDetails.addBookmark(
            marketID = paginTO?.marketID,
            type = if (paginTO?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess!!) {
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

    fun like(paginTO: PaginTO?, binding: AdapterPaginFilterBinding) {

        viewModelDetails.like(
            viewTO = ViewTO(
                paginTO?.marketID ?: "",
                GettingIP(this).deviceIpAddress ?: "",
                type = if (paginTO?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {
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


    /* onClickFilter */
    fun onClickFilter(view: View) {
        launcherFilter.launch(Intent(this@SearchFilterActivity, FilterActivity::class.java))
    }

    var launcherFilter =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {

                }
                1 -> {

//                     transferDataMarket = result.data?.getSerializableExtra(ContextApp.TRANFER_DATA_MARKET) as TransferDataMarket
                    filterTransfer = result.data?.getSerializableExtra("result") as FilterTransfer?;
                    Log.i(
                        "testFilter", "\r\n title ${filterTransfer?.title} " +
                                "\n cityId ${filterTransfer?.cityID}" +
                                "\n category ${filterTransfer?.categoryID} " +
                                "\n sub Category ${filterTransfer?.subCategoryID} " +
                                "\n type ${filterTransfer?.typePrice ?: ""}         " +
                                "\n priceFrom ${filterTransfer?.priceFrom ?: null}" +
                                "\n priceTO ${filterTransfer?.priceTo ?: null}" +
                                "\n agree  ${filterTransfer?.priceAgree ?: false}" +
                                "\n free ${filterTransfer?.free ?: false}" +
                                "\n date  ${filterTransfer?.registerDate ?: null}" +
                                "\n advanced  ${filterTransfer?.typeAdvanced ?: null}" +
                                "\n ip ${filterTransfer?.iP}"
                    )

                    filterTransfer?.title = binding.eSearch.text.toString()

                    filterTransfer?.iP = GettingIP(this@SearchFilterActivity).deviceIpAddress

                    viewModel.initData(filterTransfer ?: FilterTransfer())
                    lifecycleScope.launch {
                        viewModel.posts.collectLatest {

                            adapter?.submitData(it)
                        }
                    }
                    //            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
//            binding.swipeRefreshLayout.isRefreshing = false;
                    adapter?.refresh()
                }
                ContextApp.FINISH -> {
                    finish()
                }
            }
        }

    override fun ReturnIdOrder(order: String) {
        binding.tCheapest.text = order
        binding.recyclerOrder.isVisible = false
    }


}