package com.pedpo.pedporent.view.store.detailStore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.TransitionManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityStoreDetailBinding
import com.pedpo.pedporent.listener.OnClickStore
import com.pedpo.pedporent.model.store.ResponseStorePhotos
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.AlbumStoreAdapter
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.DialogDetailBottomStore
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.dialog.Utills
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.store.adapter.StorePagingAdapter
import com.pedpo.pedporent.view.paging.store.model.TransferData
import com.pedpo.pedporent.view.paging.store.viewModel.StorePagingViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.calendar.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class StoreDetailsActivity : AppCompatActivity(), OnClickStore,
    SwipeRefreshLayout.OnRefreshListener{

    private var storeID: String? = null
    lateinit var binding: ActivityStoreDetailBinding
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
    private var typeAPI = ContextApp.SALE;
    var paginTO: PaginTO? = null;
    var position: Int? = null;
    @Inject
    lateinit var albumAdapter: AlbumStoreAdapter;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        setToolbar()
        adapter.isIAmNot(false)

        prefJalalai = PrefManager(this@StoreDetailsActivity)
        storeID = intent?.getStringExtra(ContextApp.STORE_ID) ?: "" ;

        binding.eSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadDatas(binding.eSearch.text.toString(), type = typeAPI)
                return@OnEditorActionListener true
            }
            false
        });
        binding.icMenu.setOnClickListener {
            binding.icMenu.isEnabled = false;

            Handler(mainLooper).postDelayed({
                binding.icMenu.isEnabled = true;
            },2000)

            val dialogDetailStore = DialogDetailBottomStore().newInstance(storeID = storeID);
            dialogDetailStore.show(supportFragmentManager, "store");
        }


        initAlbum()
        pagingLastedMarket()


       initSpinnerTypeAPI()

    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.icBack.setOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "";
    }

    fun initSpinnerTypeAPI(){
        val adapterFilter = ArrayAdapter.createFromResource(this , R.array.type_api , R.layout.spinner_item_renteral)
        binding.spinnerTypePrice.adapter = adapterFilter ;
        binding.spinnerTypePrice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        typeAPI = type;

        lifecycleScope.launch {
            viewModelPaging.lastMarket(
                transferData = TransferData(
                    title = title,
                    typeAPI = type,
                    categoryID = null,
                    storeID = storeID,
                    ip = GettingIP(this@StoreDetailsActivity).deviceIpAddress
                ),
                prefApp = prefApp
            )?.collectLatest {
                adapter?.submitData(it)
            }
        }
    }

    private fun initAlbum() {
        binding.viewpagerAlbum.adapter = albumAdapter;

//        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        binding.dotsIndicator.setViewPager2(binding.viewpagerAlbum)
//        binding.viewpagerAlbum.registerOnPageChangeCallback(registerOnPage)

        profileViewModel.photosStoreUser(storeID = storeID)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseStorePhotos> {
                override fun onSuccess(dataInput: ResponseStorePhotos) {

                    if (dataInput.isSuccess == true) {
                        binding.appbar.isVisible = true;
                        binding.titleStore.text = dataInput.data?.title;
                        binding.tDescription.text = dataInput.data?.description;
                        binding.tRate.text = dataInput.data?.rateStore.toString();


                        if (binding.tDescription.lineCount > ContextApp.COUNT_LINE_STORE){
                            binding.tMore.isVisible = true;
                            binding.tDescription.maxLines = 2;
                        }else{
                            binding.tMore.isVisible = false;
                        }

                        albumAdapter.updateAdapter(dataInput.data?.images ?: emptyList());
//                        listPhotos = dataInput.data;
                    } else {
                        Toast.makeText(
                            this@StoreDetailsActivity,
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
        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        adapter = StorePagingAdapter(iAmNot = true ,prefApp = prefApp, lifecycleOwner = this);
        adapter?.onClickStore = this;

        binding.recycler.adapter = adapter?.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter?.retry()
            }
        )

        lifecycleScope.launch {
            viewModelPaging.lastMarket(
                transferData = TransferData(
                    typeAPI = typeAPI,
                    categoryID = null,
                    storeID = storeID,
                    ip = GettingIP(this@StoreDetailsActivity).deviceIpAddress
                ),
                prefApp = prefApp
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
//                binding.included?.layoutError.isVisible = refreshState is LoadState.Error


                if (loadState.refresh is LoadState.Loading) {
//                    Log.i("testRefresh", " if : ${loadState?.refresh}")
//                    Log.i("testRefresh", "loading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if ((adapter?.itemCount ?: 0) <= 0) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
//                        binding.included?.layoutError?.isVisible = true;
                        binding.included.labelError.text = getString(R.string.no_items)
                        binding.recycler.isVisible = false;
                    } else {
                        binding.included.layoutError.isVisible = false
                        binding.recycler.isVisible = true
                    }
                } else {
//                    Log.i("testRefresh", "notLoading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if ((adapter?.itemCount ?: 0) <= 0) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
                        binding.included.layoutError.isVisible = true
                        binding.included.labelError.text = getString(R.string.no_items)
                        binding.recycler.isVisible = false
                    } else {
                        binding.included.layoutError.isVisible = false
                        binding.recycler.isVisible = true
                    }


                    if (refreshState is LoadState.Error) {

                        Log.i(
                            "testPaging",
                            "lifecycleScope ${loadState.append.endOfPaginationReached}"
                        )

                        if (!loadState.append.endOfPaginationReached) {
                            binding.included?.layoutError?.isVisible = true
                            binding.included?.labelError?.text = getString(R.string.no_items)
                            binding.recycler?.isVisible = false
                        }


                        when (refreshState.error as Exception) {
                            is HttpException ->
                                binding.included.labelError.text =
                                    getString(R.string.internal_error)
                            is IOException ->
                                binding.included.labelError.text =
                                    getString(R.string.label_no_internet)
                        }

                        val errorState = loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error

                        errorState?.let {
                            Toast.makeText(
                                this@StoreDetailsActivity,
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

//        binding.swipeRefreshLayout.setOnRefreshListener {
////            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
//            binding.swipeRefreshLayout.isRefreshing = false;
//            adapter?.refresh()
//        }
    }

    override fun onRefresh() {
        adapter?.refresh() ;
        binding.swipeRefreshLayout.isRefreshing = false ;
    }



    /*onClick Read More*/
    fun onClickSearch(view: View) {
        loadDatas(binding.eSearch.text.toString(), type = typeAPI)

    }

    /*onClick Read More*/
    fun onClickReadMore(view: View) {
        if (binding.tDescription.maxLines == ContextApp.COUNT_LINE_STORE) {
            binding.tDescription.maxLines = Integer.MAX_VALUE;
            binding.tMore.isVisible = false
            animConstraintDetails()
        } else if (binding.tDescription.maxLines == Integer.MAX_VALUE) {
            binding.tDescription.maxLines = ContextApp.COUNT_LINE_STORE;
            binding.tMore.isVisible = true
            animConstraintDetails()

        }
    }
    fun animConstraintDetails() {
        val constraintSet = ConstraintSet()
//        constraintSet.load(this, R.layout.activity_language_country)
        TransitionManager.beginDelayedTransition(binding.cons)
        constraintSet.applyTo(binding.cons)
    }

    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
            loadDatas(title = null, type = ContextApp.RENT)
//            loadDataIcons();
        }, getString(R.integer.duration_anim).toLong())
    }


    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
            loadDatas( title = null, type = ContextApp.SALE )
//            loadDataIcons();
        }, getString(R.integer.duration_anim).toLong())

    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
            loadDatas(title = null, type=  ContextApp.SERVICE)
//            loadDataIconsService()
        }, getString(R.integer.duration_anim).toLong())

    }

    override fun onClickStore(paginTO: PaginTO?, position: Int?, type: String?) {

        when (type) {
            ContextApp.DETAILS -> {
                var intent = Intent(this@StoreDetailsActivity, DetailsActivity::class.java);
                intent.putExtra(ContextApp.MARKET_ID, paginTO?.marketID);
                intent.putExtra(ContextApp.TYPE_API, paginTO?.isService);
                startActivity(intent)
            }
            ContextApp.NOTIFICATION -> {

            }
        }
    }

}