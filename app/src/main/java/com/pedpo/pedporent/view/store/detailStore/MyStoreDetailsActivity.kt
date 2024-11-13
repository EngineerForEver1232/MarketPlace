package com.pedpo.pedporent.view.store.detailStore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityMyStoreDetailBinding
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.listener.OnClickStore
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.ActiveMarketTO
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.model.store.ResponseStorePhotos
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.AlbumStoreAdapter
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.DialogDetailBottomStore
import com.pedpo.pedporent.view.dialog.MenuItemFragDialog
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.dialog.Utills
import com.pedpo.pedporent.view.editMarket.EditGeneralActivity
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.store.adapter.StorePagingAdapter
import com.pedpo.pedporent.view.paging.store.model.TransferData
import com.pedpo.pedporent.view.paging.store.viewModel.StorePagingViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.calendar.CalendarActivity
import com.pedpo.pedporent.widget.calendar.utils.AppContents
import com.pedpo.pedporent.widget.calendar.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class MyStoreDetailsActivity : AppCompatActivity(), ClickAdapterPaging, ReturnContent , OnClickStore,
    SwipeRefreshLayout.OnRefreshListener{

    private var storeID: String? = null;
    lateinit var binding: ActivityMyStoreDetailBinding


    private val viewModelPaging: StorePagingViewModel by viewModels();
    private val profileViewModel: ProfileViewModel by viewModels();

    private var prefJalalai: PrefManager? = null;

    @Inject
    lateinit var utills: Utills;
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var prefApp: PrefApp;
    @Inject
    lateinit var adapter: StorePagingAdapter
    @Inject
    lateinit var numberFormat:NumberFormatDigits
    private var typeAPI = ContextApp.SALE;

    var paginTO: PaginTO? = null;
    var position: Int? = null;


    @Inject
    lateinit var albumAdapter: AlbumStoreAdapter;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyStoreDetailBinding.inflate(layoutInflater)
        binding.listener = this ;
        setContentView(binding.root) ;
        binding.layout.swipeRefreshLayout.setOnRefreshListener(this)
        setToolbar()
        adapter.isIAmNot(true)

        prefJalalai = PrefManager(this@MyStoreDetailsActivity)
        storeID = intent?.getStringExtra(ContextApp.STORE_ID) ?: ""


        binding.layout.icMenu.setOnClickListener {
            val dialogDetailStore = DialogDetailBottomStore().newInstance(storeID = storeID)
            dialogDetailStore.show(supportFragmentManager, "store")
        }

        binding.layout.eSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadDatas(title = title?.toString(), type = typeAPI)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        initAlbum()
        pagingLastedMarket()

        initSpinnerTypeAPI()

    }

    private fun setToolbar() {
        setSupportActionBar(binding.layout.toolbar)
        binding.layout.icBack.setOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    fun initSpinnerTypeAPI(){
        val adapterFilter = ArrayAdapter.createFromResource(this , R.array.type_api , R.layout.spinner_item_renteral)
        binding.layout.spinnerTypePrice.adapter = adapterFilter
        binding.layout.spinnerTypePrice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    0 -> loadDatas(title = null, type = ContextApp.RENT)
                    1 -> loadDatas(title = null, type = ContextApp.SALE)
                    else -> loadDatas(title = null, type = ContextApp.SERVICE)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }


    fun loadDatas(title: String?, type: String) {
        typeAPI = type

        lifecycleScope.launch {
            viewModelPaging.lastMarket(
                transferData = TransferData(
                    title = title,
                    typeAPI = type,
                    categoryID = null,
                    storeID = storeID,
                    ip = GettingIP(this@MyStoreDetailsActivity).deviceIpAddress
                ),
                prefApp = prefApp
            )?.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAlbum() {
//        albumAdapter?.iOnClickDialog = this

        binding.layout.viewpagerAlbum.adapter = albumAdapter;

//        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        binding.layout.dotsIndicator.setViewPager2(binding.layout.viewpagerAlbum)
//        binding.layout.
//        viewpagerAlbum.registerOnPageChangeCallback(registerOnPage)

        profileViewModel.photosStore()?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseStorePhotos> {
                override fun onSuccess(dataInput: ResponseStorePhotos) {

                    if (dataInput.isSuccess == true) {
                        binding.layout.appbar.isVisible = true;
                        binding.layout.titleStore.text = dataInput.data?.title;
                        binding.layout.tDescription.text = dataInput.data?.description;
                        binding.layout.tRate.text = dataInput.data?.rateStore.toString();


                        if (binding.layout.tDescription.lineCount > ContextApp.COUNT_LINE_STORE){
                            binding.layout.tMore.isVisible = true;
                            binding.layout.tDescription.maxLines = 2;
                        }else{
                            binding.layout.tMore.isVisible = false;
                        }

                        albumAdapter.updateAdapter(dataInput.data?.images ?: emptyList());
//                        listPhotos = dataInput.data;
                    } else {
                        Toast.makeText(
                            this@MyStoreDetailsActivity,
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onException(exception: Exception) {

                }
            })
        )

    }


    fun pagingLastedMarket() {
        binding.layout.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapter.onClickStore = this;

        binding.layout.recycler.adapter = adapter.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter.retry()
            }
        )

        lifecycleScope.launch {
            viewModelPaging.lastMarket(
                transferData = TransferData(
                    typeAPI = typeAPI,
                    categoryID = null,
                    storeID = storeID,
                    ip = GettingIP(this@MyStoreDetailsActivity).deviceIpAddress
                ),
                prefApp = prefApp
            )?.collectLatest {
                adapter.submitData(it)

            }
        }


        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->

                val refreshState = loadState.refresh;
//                Log.i("testRefresh", " else : ${loadState?.refresh} ${refreshState is LoadState.Error}" )
                binding.layout.recycler.isVisible = refreshState !is LoadState.Error
//                binding.layout.
//                swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
//                binding?.included?.layoutError.isVisible = refreshState is LoadState.Error


                if (loadState?.refresh is LoadState.Loading) {
//                    Log.i("testRefresh", " if : ${loadState?.refresh}")
//                    Log.i("testRefresh", "loading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if ((adapter?.itemCount ?: 0) <= 0) {
//                    binding.layout.
                        //                    progress.isVisible = true
//                        binding.layout.
                        //                        swipeRefreshLayout.isRefreshing = true
//                        binding?.included?.layoutError?.isVisible = true;
                        binding.layout?.included?.labelError?.text = getString(R.string.no_items)
                        binding.layout.recycler.isVisible = false;
                    } else {
                        binding.layout?.included?.layoutError?.isVisible = false
                        binding.layout?.recycler?.isVisible = true
                    }
                } else {
//                    Log.i("testRefresh", "notLoading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if ((adapter?.itemCount ?: 0) <= 0) {
//                    binding.layout.
                        //                    progress.isVisible = true
//                        binding.layout.
                        //                        swipeRefreshLayout.isRefreshing = true
                        binding.layout?.included?.layoutError?.isVisible = true
                        binding.layout?.included?.labelError?.text = getString(R.string.no_items)
                        binding.layout?.recycler?.isVisible = false
                    } else {
                        binding.layout?.included?.layoutError?.isVisible = false
                        binding.layout?.recycler?.isVisible = true
                    }


                    if (refreshState is LoadState.Error) {

                        Log.i(
                            "testPaging",
                            "lifecycleScope ${loadState.append.endOfPaginationReached}"
                        )

                        if (!loadState.append.endOfPaginationReached) {
                            binding.layout.included.layoutError.isVisible = true
                            binding.layout?.included?.labelError?.text = getString(R.string.no_items)
                            binding.layout?.recycler?.isVisible = false
                        }


                        when (refreshState.error as Exception) {
                            is HttpException ->
                                binding.layout?.included.labelError.text =
                                    getString(R.string.internal_error)
                            is IOException ->
                                binding.layout?.included.labelError.text =
                                    getString(R.string.label_no_internet)
                        }

                        val errorState = loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error

                        errorState?.let {
                            Toast.makeText(
                                this@MyStoreDetailsActivity,
                                ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        }

        binding.layout.included.reloadPostsBtn.setOnClickListener {
            adapter.refresh();
        }

//        binding.layout.
    //        swipeRefreshLayout.setOnRefreshListener {
////            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
//            binding.layout.
    //            swipeRefreshLayout.isRefreshing = false;
//            adapter?.refresh()
//        }
    }

    override fun onRefresh() {
        adapter.refresh()
        binding.layout.swipeRefreshLayout.isRefreshing = false;
    }

    override fun onClickAdapterPaging(
        paginTO: PaginTO?,
        action: String,
        binding: AdapterPaginBinding
    ) {

    }


    override fun onClickStore(paginTO: PaginTO?, position: Int?, type: String?) {
        when (type) {
            ContextApp.DETAILS -> {
                val intent = Intent(this@MyStoreDetailsActivity, DetailsActivity::class.java)
                intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID)
                intent.putExtra(ContextApp.TYPE_API, paginTO?.isService)
                startActivity(intent)
            }
            ContextApp.MENU -> {
                this.paginTO = paginTO
                this.position = position

                val menuFrag = MenuItemFragDialog().newInstance(
                    isActive = paginTO?.isActive ?: false,
                    marketID = paginTO?.marketID)
                menuFrag.returnContent = this
                menuFrag.show(supportFragmentManager, ContextApp.MENU)
            }
            ContextApp.NOTIFICATION -> {

            }
        }
    }

    override fun returnContent(content: String?) {
        when (content) {
            ContextApp.DEACTIVE -> {
                dateActive(paginTO = paginTO,
                    if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
                )
            }
            ContextApp.ACTIVE -> {
                active(paginTO?.marketID ?: "",
                    if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
                )
            }
            ContextApp.DELETE -> {
                utills.showDialogPositive(
                    getString(R.string.are_you_sure_delete),
                    getString(R.string.yes),
                    getString(R.string.no)
                )
                    .observe(this, Observer {
                        if (it == true) {
                            deleteMarket(paginTO);
                        }
                    })
            }
            ContextApp.EDIT -> {
//                var intent = Intent(this, EditMarketActivity::class.java);
                var intent = Intent(this@MyStoreDetailsActivity, EditGeneralActivity::class.java);
                intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
                intent.putExtra(ContextApp.TYPE_API,
                    if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
                );
                startActivity(intent);
            }
        }
    }

    private fun active(marketID: String, type: String) {
        showProgressBar.show(supportFragmentManager)
        profileViewModel.activeMarket(marketID, type)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {

                        prefJalalai?.clear()
//                        myItemsLoadData(typeAPI)
                        Toast.makeText(this@MyStoreDetailsActivity, dataInput.message, Toast.LENGTH_SHORT)
                            .show();
                        loadDatas(title = null, type = typeAPI)

                    } else {
                        Toast.makeText(this@MyStoreDetailsActivity, dataInput?.message, Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    fun deleteMarket(paginTO: PaginTO?) {
        val myMarket = MyMarkets();
        myMarket.marketID = paginTO?.marketID;
        myMarket.isService = paginTO?.isService;

        profileViewModel.deleteMarket(myMarket)
            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    if (dataInput.isSuccess == true) {
                        loadDatas(title = null, type = typeAPI)
                    }

                    Toast.makeText(this@MyStoreDetailsActivity , dataInput.message.toString(),Toast.LENGTH_SHORT).show()

                    Log.i("testDelete", "onSuccess: ${dataInput.isSuccess}")
                }

                override fun onException(exception: Exception) {
                    Log.e("testDelete", "onSuccess: ${exception.message}")
                }
            }))
    }

    fun deActive(activeMarketTO: ActiveMarketTO) {
        Log.i("testCalendar", "deActiveMarket: ${activeMarketTO.MarketID}")

        profileViewModel.deActiveMarket(activeMarketTO = activeMarketTO)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    if (dataInput.isSuccess == true) {
                        Toast.makeText(this@MyStoreDetailsActivity, dataInput.message, Toast.LENGTH_SHORT)
                            .show()
                        loadDatas(title = null, type = typeAPI)
                    } else {

                    }

                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    fun dateActive(paginTO:  PaginTO?, type: String) {

        prefJalalai?.clear();

        var intent = Intent(this@MyStoreDetailsActivity, CalendarActivity::class.java)
        intent.putExtra(ContextApp.START_DATE, paginTO?.startDate)
        intent.putExtra(ContextApp.END_DATE, paginTO?.endDate)
        intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID)
        intent.putExtra(ContextApp.TYPE, type)
        launcher.launch(intent);

    }



    var launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                }
                AppContents.RESULT_SET_CALENDAR_POSTER -> {
                    var start = result.data?.getStringExtra(AppContents.FROM_CALENDAR_MILADI);
                    var end = result.data?.getStringExtra(AppContents.TO_CALENDAR_MILADI);

                    deActive(
                        ActiveMarketTO(
                            paginTO?.marketID ?: "",
                            numberFormat.toNumberEnlish(start?:"")?:"",
                            numberFormat.toNumberEnlish(end?:"")?:"" ,
                            typeAPI = if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
                        )
                    )
                    Log.i("testDeactive", "result start : ${start}")
                    Log.i("testDeactive", "result end : ${end}")

                }
            }
        }

}