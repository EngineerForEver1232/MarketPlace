//package com.pedpo.pedporent.view.store.detailStore
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.View
//import android.view.animation.AnimationUtils
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.core.view.isVisible
//import androidx.lifecycle.Observer
//import androidx.lifecycle.lifecycleScope
//import androidx.paging.LoadState
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.google.android.material.textview.MaterialTextView
//import com.pedpo.pedporent.R
//import com.pedpo.pedporent.databinding.ActivityMyStoreDetailBinding
//import com.pedpo.pedporent.databinding.AdapterPaginBinding
//import com.pedpo.pedporent.listener.ClickAdapterPaging
//import com.pedpo.pedporent.listener.OnClickStore
//import com.pedpo.pedporent.listener.ReturnContent
//import com.pedpo.pedporent.model.ActiveMarketTO
//import com.pedpo.pedporent.model.ResponseTO
//import com.pedpo.pedporent.model.myItems.MyMarkets
//import com.pedpo.pedporent.model.store.ResponseStorePhotos
//import com.pedpo.pedporent.utills.ContextApp
//import com.pedpo.pedporent.utills.CustomObserver
//import com.pedpo.pedporent.utills.GettingIP
//import com.pedpo.pedporent.utills.UtillsApp
//import com.pedpo.pedporent.utills.permission.PrefApp
//import com.pedpo.pedporent.view.adapter.AlbumStoreAdapter
//import com.pedpo.pedporent.view.details.DetailsActivity
//import com.pedpo.pedporent.view.dialog.DialogDetailBottomStore
//import com.pedpo.pedporent.view.dialog.MenuItemFragDialog
//import com.pedpo.pedporent.view.dialog.ShowProgressBar
//import com.pedpo.pedporent.view.dialog.Utills
//import com.pedpo.pedporent.view.editMarket.EditGeneralActivity
//import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
//import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
//import com.pedpo.pedporent.view.paging.store.adapter.StorePagingAdapter
//import com.pedpo.pedporent.view.paging.store.model.TransferData
//import com.pedpo.pedporent.view.paging.store.viewModel.StorePagingViewModel
//import com.pedpo.pedporent.viewModel.ProfileViewModel
//import com.pedpo.pedporent.widget.calendar.CalendarActivity
//import com.pedpo.pedporent.widget.calendar.utils.AppContents
//import com.pedpo.pedporent.widget.calendar.utils.PrefManager
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//import java.util.ArrayList
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class MyStoreDetailsActivityOLD : AppCompatActivity(), ClickAdapterPaging, ReturnContent , OnClickStore,
//    SwipeRefreshLayout.OnRefreshListener{
//
//    private var storeID: String? = null;
//    lateinit var binding: ActivityMyStoreDetailBinding
//
//
//    private val viewModelPaging: StorePagingViewModel by viewModels();
//    private val profileViewModel: ProfileViewModel by viewModels();
//
//    private var prefJalalai: PrefManager? = null;
//
//    @Inject
//    lateinit var utills: Utills;
//
//    @Inject
//    lateinit var showProgressBar: ShowProgressBar;
//
//    @Inject
//    lateinit var prefApp: PrefApp;
//
//    private var adapter: StorePagingAdapter? = null;
//
//    private var typeAPI = ContextApp.SALE;
//    var listMyMyItems = ArrayList<PaginTO>();
//
//    var paginTO: PaginTO? = null;
//    var position: Int? = null;
//
//
//    @Inject
//    lateinit var albumAdapter: AlbumStoreAdapter;
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMyStoreDetailBinding.inflate(layoutInflater)
//        binding.listener = this;
//        setContentView(binding.root)
//        binding.swipeRefreshLayout.setOnRefreshListener(this)
//        setToolbar()
//
//        prefJalalai = PrefManager(this@MyStoreDetailsActivityOLD)
//        storeID = intent?.getStringExtra(ContextApp.STORE_ID) ?: "" ;
//
//
//        binding.icMenu.setOnClickListener {
//            val dialogDetailStore = DialogDetailBottomStore().newInstance(storeID = storeID);
//            dialogDetailStore.show(supportFragmentManager, "store");
//        }
//
//        binding.eSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                loadDatas(title = title?.toString(), type = typeAPI)
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })
//
//        initAlbum()
//        pagingLastedMarket()
//
//    }
//
//    private fun setToolbar() {
//        setSupportActionBar(binding.toolbar)
//        binding.icBack.setOnClickListener { finish() }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = "";
//    }
//
//
//    fun loadDatas(title: String?, type: String) {
//        typeAPI = type;
//
//        lifecycleScope.launch {
//            viewModelPaging.lastMarket(
//                transferData = TransferData(
//                    title = title,
//                    typeAPI = type,
//                    categoryID = null,
//                    storeID = storeID,
//                    ip = GettingIP(this@MyStoreDetailsActivityOLD).deviceIpAddress
//                ),
//                prefApp = prefApp
//            )?.collectLatest {
//                adapter?.submitData(it)
//            }
//        }
//    }
//
//    private fun initAlbum() {
////        albumAdapter?.iOnClickDialog = this
//
//        binding.viewpagerAlbum.adapter = albumAdapter;
//
////        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
//        binding.dotsIndicator.setViewPager2(binding.viewpagerAlbum)
////        binding.viewpagerAlbum.registerOnPageChangeCallback(registerOnPage)
//
//        profileViewModel.photosStore()?.observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<ResponseStorePhotos> {
//                override fun onSuccess(dataInput: ResponseStorePhotos) {
//
//                    if (dataInput.isSuccess == true) {
//                        binding.appbar.isVisible = true;
//                        binding.titleStore.text = dataInput.data?.title;
//
//                        albumAdapter.updateAdapter(dataInput.data?.images ?: emptyList());
////                        listPhotos = dataInput.data;
//                    } else {
//                        Toast.makeText(
//                            this@MyStoreDetailsActivityOLD,
//                            dataInput.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                }
//
//                override fun onException(exception: Exception) {
//
//                }
//            })
//        )
//
//    }
//
//    fun pagingLastedMarket() {
//        binding.recycler.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        adapter = StorePagingAdapter(prefApp = prefApp, lifecycleOwner = this);
//        adapter?.clickAdapterPaging = this;
//        adapter?.onClickStore = this;
//
//        binding.recycler.adapter = adapter?.withLoadStateFooter(
//            footer = CustomLoadStateAdapter {
//                adapter?.retry()
//            }
//        )
//
//        lifecycleScope.launch {
//            viewModelPaging.lastMarket(
//                transferData = TransferData(
//                    typeAPI = typeAPI,
//                    categoryID = null,
//                    storeID = storeID,
//                    ip = GettingIP(this@MyStoreDetailsActivityOLD).deviceIpAddress
//                ),
//                prefApp = prefApp
//            )?.collectLatest {
//                adapter?.submitData(it)
//
//            }
//        }
//
//
//        lifecycleScope.launch {
//            adapter?.loadStateFlow?.collect { loadState ->
//
//                val refreshState = loadState.refresh;
////                Log.i("testRefresh", " else : ${loadState?.refresh} ${refreshState is LoadState.Error}" )
//                binding.recycler.isVisible = refreshState !is LoadState.Error
////                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
////                binding?.included?.layoutError.isVisible = refreshState is LoadState.Error
//
//
//                if (loadState?.refresh is LoadState.Loading) {
////                    Log.i("testRefresh", " if : ${loadState?.refresh}")
////                    Log.i("testRefresh", "loading size : ${adapter?.snapshot()?.isEmpty()} ")
//
//                    if ((adapter?.itemCount ?: 0) <= 0) {
////                    binding.progress.isVisible = true
////                        binding.swipeRefreshLayout.isRefreshing = true
////                        binding?.included?.layoutError?.isVisible = true;
//                        binding?.included?.labelError?.text = getString(R.string.no_items)
//                        binding.recycler.isVisible = false;
//                    } else {
//                        binding?.included?.layoutError?.isVisible = false
//                        binding?.recycler?.isVisible = true
//                    }
//                } else {
////                    Log.i("testRefresh", "notLoading size : ${adapter?.snapshot()?.isEmpty()} ")
//
//                    if ((adapter?.itemCount ?: 0) <= 0) {
////                    binding.progress.isVisible = true
////                        binding.swipeRefreshLayout.isRefreshing = true
//                        binding?.included?.layoutError?.isVisible = true
//                        binding?.included?.labelError?.text = getString(R.string.no_items)
//                        binding?.recycler?.isVisible = false
//                    } else {
//                        binding?.included?.layoutError?.isVisible = false
//                        binding?.recycler?.isVisible = true
//                    }
//
//
//                    if (refreshState is LoadState.Error) {
//
//                        Log.i(
//                            "testPaging",
//                            "lifecycleScope ${loadState.append.endOfPaginationReached}"
//                        )
//
//                        if (!loadState.append.endOfPaginationReached) {
//                            binding?.included?.layoutError?.isVisible = true
//                            binding?.included?.labelError?.text = getString(R.string.no_items)
//                            binding?.recycler?.isVisible = false
//                        }
//
//
//                        when (refreshState.error as Exception) {
//                            is HttpException ->
//                                binding?.included.labelError.text =
//                                    getString(R.string.internal_error)
//                            is IOException ->
//                                binding?.included.labelError.text =
//                                    getString(R.string.label_no_internet)
//                        }
//
//                        val errorState = loadState.append as? LoadState.Error
//                            ?: loadState.prepend as? LoadState.Error
//
//                        errorState?.let {
//                            Toast.makeText(
//                                this@MyStoreDetailsActivityOLD,
//                                ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//
//
//            }
//        }
//
//        binding?.included?.reloadPostsBtn.setOnClickListener {
//            adapter?.refresh();
//        }
//
////        binding.swipeRefreshLayout.setOnRefreshListener {
//////            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
////            binding.swipeRefreshLayout.isRefreshing = false;
////            adapter?.refresh()
////        }
//    }
//
//    override fun onRefresh() {
//        adapter?.refresh()
//        binding.swipeRefreshLayout.isRefreshing = false;
//    }
//
//    override fun onClickAdapterPaging(
//        paginTO: PaginTO?,
//        action: String,
//        binding: AdapterPaginBinding
//    ) {
//
//    }
//
//    /*OnClick onClick Button Category Rent */
//    fun onClickBtnCategoryRent(view: View) {
//        animationBtnRent()
//    }
//
//    fun animationBtnRent() {
//        if (binding.tr.tag != null && binding.tr.tag.equals("true"))
//            return;
//        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(title = null, type = ContextApp.RENT)
////            loadDataIcons();
//        }, getString(R.integer.duration_anim).toLong())
//
//        if (binding.tS.tag != null && binding.tS.tag.equals("true")) {
//            binding.lineBottomSale.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_left
//                )
//            )
//            binding.lineBottomRent.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_refresh_left
//                )
//            )
//
//        } else if (binding.tService.tag != null && binding.tService.tag.equals("true")) {
//            binding.lineBottomService.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_left
//                )
//            )
//            binding.lineBottomRent.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_refresh_left
//                )
//            )
//        }
//
//        selectBtnCategory(binding.tr, binding.img, binding.lineBottomRent)
//
//        unSelectBtnCategory(binding.tS, binding.imgSale, binding.lineBottomSale)
//        unSelectBtnCategory(binding.tService, binding.imgService, binding.lineBottomService)
//    }
//
//    /*OnClick onClick Button Category Sale */
//    fun onClickBtnCategorySale(view: View) {
//        if (binding.tS.tag != null && binding.tS.tag.equals("true"))
//            return;
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas( title = null, type = ContextApp.SALE )
////            loadDataIcons();
//        }, getString(R.integer.duration_anim).toLong())
//
//
//        if (binding.tr.tag == null || binding.tr.tag.equals("true")) {
//            binding.lineBottomSale.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_refresh_right
//                )
//            )
//            binding.lineBottomRent.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_right
//                )
//            )
//        } else if (binding.tService.tag != null && binding.tService.tag.equals("true")) {
//            binding.lineBottomSale.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_refresh_left
//                )
//            )
//            binding.lineBottomService.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_left
//                )
//            )
//        }
//
//
//        selectBtnCategory(binding.tS, binding.imgSale, binding.lineBottomSale)
//
//        unSelectBtnCategory(binding.tr, binding.img, binding.lineBottomRent)
//        unSelectBtnCategory(binding.tService, binding.imgService, binding.lineBottomService)
//    }
//
//    /*OnClick onClick Button Category Service */
//    fun onClickBtnCategoryService(view: View) {
//        if (binding.tService.tag != null && binding.tService.tag.equals("true"))
//            return;
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(title = null, type=  ContextApp.SERVICE)
////            loadDataIconsService()
//        }, getString(R.integer.duration_anim).toLong())
//
//        if (binding.tr.tag == null || binding.tr.tag.equals("true")) {
//            binding.lineBottomService.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_refresh_right
//                )
//            )
//            binding.lineBottomRent.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_right
//                )
//            )
//        } else if (binding.tS.tag != null && binding.tS.tag.equals("true")) {
//            binding.lineBottomService.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_refresh_right
//                )
//            )
//            binding.lineBottomSale.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@MyStoreDetailsActivityOLD,
//                    R.anim.trans_line_right
//                )
//            )
//        }
//
//
//        selectBtnCategory(binding.tService, binding.imgService, binding.lineBottomService)
//
//        unSelectBtnCategory(binding.tr, binding.img, binding.lineBottomRent)
//        unSelectBtnCategory(binding.tS, binding.imgSale, binding.lineBottomSale)
//    }
//
//
//    fun selectBtnCategory(textview: MaterialTextView?, imageView: ImageView?, view: View?) {
//        textview?.tag = "true"
//        textview?.setTextColor(
//            ContextCompat.getColor(
//                this@MyStoreDetailsActivityOLD,
//                R.color.colorPrimary
//            )
//        )
//        imageView?.setColorFilter(
//            ContextCompat.getColor(
//                this@MyStoreDetailsActivityOLD,
//                R.color.colorPrimary
//            )
//        )
//        view?.isVisible = true
//    }
//
//    fun unSelectBtnCategory(textview: MaterialTextView?, imageView: ImageView?, view: View?) {
//        textview?.tag = "false"
//        textview?.setTextColor(
//            ContextCompat.getColor(
//                this@MyStoreDetailsActivityOLD,
//                R.color.text_category_type
//            )
//        )
//        imageView?.setColorFilter(
//            ContextCompat.getColor(
//                this@MyStoreDetailsActivityOLD,
//                R.color.text_category_type
//            )
//        )
//        view?.isVisible = false
//    }
//
//    override fun onClickStore(paginTO: PaginTO?, position: Int?, type: String?) {
//        when (type) {
//            ContextApp.DETAILS -> {
//                var intent = Intent(this@MyStoreDetailsActivityOLD, DetailsActivity::class.java);
//                intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
//                intent.putExtra(ContextApp.TYPE_API, paginTO?.isService);
//                startActivity(intent)
//            }
//            ContextApp.MENU -> {
//                this.paginTO = paginTO;
//                this.position = position;
//
//                var menuFrag = MenuItemFragDialog().newInstance(paginTO?.isActive ?: false);
//                menuFrag.returnContent = this;
//                menuFrag?.show(supportFragmentManager, ContextApp.MENU);
//            }
//            ContextApp.NOTIFICATION -> {
//
//            }
//        }
//    }
//
//    override fun returnContent(content: String?) {
//        when (content) {
//            ContextApp.DEACTIVE -> {
//                dateActive(paginTO = paginTO,
//                    if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
//                )
//            }
//            ContextApp.ACTIVE -> {
//                active(paginTO?.marketID ?: "",
//                    if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
//                )
//            }
//            ContextApp.DELETE -> {
//                utills.showDialogPositive(
//                    getString(R.string.are_you_sure_delete),
//                    getString(R.string.yes),
//                    getString(R.string.no)
//                )
//                    .observe(this, Observer {
//                        if (it == true) {
//                            deleteMarket(paginTO);
//                        }
//                    })
//            }
//            ContextApp.EDIT -> {
////                var intent = Intent(this, EditMarketActivity::class.java);
//                var intent = Intent(this@MyStoreDetailsActivityOLD, EditGeneralActivity::class.java);
//                intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
//                intent.putExtra(ContextApp.TYPE_API,
//                    if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
//                );
//                startActivity(intent);
//            }
//        }
//    }
//
//    private fun active(marketID: String, type: String) {
//        showProgressBar.show(supportFragmentManager)
//        profileViewModel.activeMarket(marketID, type)?.observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
//                override fun onSuccess(dataInput: ResponseTO) {
//                    showProgressBar.dismiss()
//                    if (dataInput.isSuccess == true) {
//
//                        prefJalalai?.clear()
////                        myItemsLoadData(typeAPI)
//                        Toast.makeText(this@MyStoreDetailsActivityOLD, dataInput.message, Toast.LENGTH_SHORT)
//                            .show();
//                        loadDatas(title = null, type = typeAPI)
//
//                    } else {
//                        Toast.makeText(this@MyStoreDetailsActivityOLD, dataInput?.message, Toast.LENGTH_SHORT)
//                            .show()
//
//                    }
//                }
//
//                override fun onException(exception: Exception) {
//
//                }
//
//            })
//        )
//    }
//
//    fun deleteMarket(paginTO: PaginTO?) {
//        val myMarket = MyMarkets();
//        myMarket.marketID = paginTO?.marketID;
//        myMarket.isService = paginTO?.isService;
//
//        profileViewModel.deleteMarket(myMarket)
//            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
//                override fun onSuccess(dataInput: ResponseTO) {
//                    if (dataInput.isSuccess == true) {
//                        listMyMyItems.remove(paginTO)
//                        adapter?.notifyItemRemoved(position!!)
//                        adapter?.notifyDataSetChanged()
//                    }
//
//
//                    Log.i("testDelete", "onSuccess: ${dataInput.isSuccess}")
//                }
//
//                override fun onException(exception: Exception) {
//                    Log.e("testDelete", "onSuccess: ${exception.message}")
//                }
//            }))
//    }
//
//    fun deActive(activeMarketTO: ActiveMarketTO) {
//        Log.i("testCalendar", "deActiveMarket: ${activeMarketTO?.MarketID}")
//
//        profileViewModel?.deActiveMarket(activeMarketTO = activeMarketTO)?.observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
//                override fun onSuccess(dataInput: ResponseTO) {
//                    if (dataInput?.isSuccess == true) {
////                        myItemsLoadData(typeAPI)
//                        Toast.makeText(this@MyStoreDetailsActivityOLD, dataInput.message, Toast.LENGTH_SHORT)
//                            .show()
//                        loadDatas(title = null, type = typeAPI)
//                    } else {
//
//                    }
//
//                }
//
//                override fun onException(exception: Exception) {
//
//                }
//
//            })
//        )
//    }
//
//    fun dateActive(paginTO:  PaginTO?, type: String) {
//
//        prefJalalai?.clear();
//
//        var intent = Intent(this@MyStoreDetailsActivityOLD, CalendarActivity::class.java)
//        intent.putExtra(ContextApp.START_DATE, paginTO?.startDate)
//        intent.putExtra(ContextApp.END_DATE, paginTO?.endDate)
//        intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID)
//        intent.putExtra(ContextApp.TYPE, type)
//        launcher.launch(intent);
//
//    }
//
//    var launcher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            when (result.resultCode) {
//                Activity.RESULT_OK -> {
//                }
//                AppContents.RESULT_SET_CALENDAR_POSTER -> {
//                    var start = result.data?.getStringExtra(AppContents.FROM_CALENDAR_MILADI);
//                    var end = result.data?.getStringExtra(AppContents.TO_CALENDAR_MILADI);
//
//                    deActive(
//                        ActiveMarketTO(
//                            paginTO?.marketID ?: "",
//                            start ?: "",
//                            end ?: "",
//                            typeAPI = if (typeAPI != ContextApp.SERVICE) ContextApp.MARKET else ContextApp.SERVICE
//                        )
//                    )
//                    Log.i("testDeactive", "result start : ${start}")
//                    Log.i("testDeactive", "result end : ${end}")
//
//                }
//            }
//        }
//
//}