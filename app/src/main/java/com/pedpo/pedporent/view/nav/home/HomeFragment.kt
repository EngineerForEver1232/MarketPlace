package com.pedpo.pedporent.view.nav.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.databinding.AdapterPaginNewBinding
import com.pedpo.pedporent.databinding.FragmentHomeBinding
import com.pedpo.pedporent.listener.ClickAdapterVisited
import com.pedpo.pedporent.listener.ClickIconCategory
import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.model.BookmarkData
import com.pedpo.pedporent.model.LikeData
import com.pedpo.pedporent.model.ViewTO
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.market.category.CategoryTO
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.*
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.details.DetailsNeighborActivity
import com.pedpo.pedporent.view.dialog.ShowAreaDialog
import com.pedpo.pedporent.view.dialog.progress.ShowLoadingPedpo
import com.pedpo.pedporent.view.filter.SearchFilterActivity
import com.pedpo.pedporent.view.map.MapActivity
import com.pedpo.pedporent.view.paging.search.activity.SearchActivity
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.ClickAdapterPaging
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.LastedPagingAdapterNew
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.viewModel.LastMarketViewModel2
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.BaseConstants
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.viewModel.CategoryViewModel
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.viewModel.HomeViewModel
import com.pedpo.pedporent.widget.CustomItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), ClickAdapterVisited, BaseConstants, ClickIconCategory,
    ClickAdapterPaging,
    SwipeRefreshLayout.OnRefreshListener, OnReturnPlace {

    lateinit var binding: FragmentHomeBinding
    @Inject
    lateinit var posterAdapter: PosterStoreAdapter
    private val viewModelDetails: DetailsViewModel by viewModels()
    @Inject
    lateinit var showLoadingPedpo: ShowLoadingPedpo
    @Inject
    lateinit var serviceAPI: ServiceAPI
    @Inject
    lateinit var prefApp: PrefApp
    @Inject
    lateinit var adapter: LastedPagingAdapterNew

    private val homeViewModel: HomeViewModel by viewModels()
    private val viewModelCategory: CategoryViewModel by viewModels()
    private val viewModel: LastMarketViewModel2 by viewModels()

    private var typeAPI = ContextApp.RENT
    private var categoryIdLast: String? = null
    private val progressCheck = arrayOf(false, false)


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

//        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK ;
//        when (currentNightMode) {
//            Configuration.UI_MODE_NIGHT_NO -> {
//                binding.recycler.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background_home))
//                binding.collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background_home))
//            } // Night mode is not active, we're using the light theme
//            Configuration.UI_MODE_NIGHT_YES -> {
////                iconsAdapter.updateAdapter()
//                binding.recycler.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
//                binding.collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
//            } // Night mode is active, we're using dark theme
//        }
    }


    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        binding.lifecycleOwner = this
        binding.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingPedpo.show(fragmentManager = childFragmentManager)

        initPagerPoster()
        loadDataPoster()


        initPagerIcons()
        loadDataIcons()


        binding.linearSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        pagingLastedMarket()

        binding.tPlace.text = prefApp.getNameCity()


        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        MyMutable.mutableCity.observe(viewLifecycleOwner) {
            binding.tPlace.text = it
            adapter.refresh()
        }

        var firstShowFab = false ;
        val animation = AnimationUtils.loadAnimation(activity, R.anim.transfer_fab_up)

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.fabUp.show()
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })


        val animationDown = AnimationUtils.loadAnimation(context,R.anim.transfer_fab_down)
        animationDown.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.fabUp.hide()

            }

            override fun onAnimationRepeat(p0: Animation?) {}

        })

//        binding.fabUp.hide()
        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.i("testFab", "onScrolled: ${recyclerView.pivotY} ${recyclerView.translationY} $dy")
                if (dy > 0) {

                    binding.fabUp.hide()
                } else {
                    if (firstShowFab){
                        binding.fabUp.show()
                    }
                    firstShowFab=true;
                }


                super.onScrolled(recyclerView, dx, dy)
            }
        })


        binding.fabUp.setOnClickListener {
            binding.recycler.layoutManager?.scrollToPosition(0)
            binding.fabUp.hide()
            firstShowFab=false;
        }

    }


    fun onClickSearchFilter(view: View) {
        startActivity(Intent(requireContext(), SearchFilterActivity::class.java))
    }


    fun pagingLastedMarket() {
        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
//        adapter = LastedPagingAdapterNew(prefApp = prefApp, lifecycleOwner = viewLifecycleOwner  );
        adapter.clickAdapterPaging = this;

        binding.recycler.adapter = adapter.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter.retry()
            }
        )

        checkProgress(0)

        lifecycleScope.launch {
            viewModel.lastMarket(
                categoryID = categoryID ,
                GettingIP(requireContext()).deviceIpAddress, prefApp = prefApp , typeAPI
            )?.collectLatest {
                adapter.submitData(it)
            }
        }


        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->

                val refreshState = loadState.refresh
                Log.i("testRefresh", " else : ${loadState.refresh} ${refreshState is LoadState.Error}" )
                binding.recycler.isVisible = refreshState !is LoadState.Error
//                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
//                binding.included?.layoutError.isVisible = refreshState is LoadState.Error

                if (loadState.refresh is LoadState.Loading) {
//                    Log.i("testRefresh", " if : ${loadState?.refresh}")
//                    Log.i("testRefresh", "loading size : ${adapter?.snapshot()?.isEmpty()} ")
//                    binding.included.layoutError.isVisible = true
                    binding.included.labelError.text = getString(R.string.please_wait)
//                    binding.recycler.isVisible = false

                    if ((adapter.itemCount ?: 0) <= 0) {
//                    binding.progress.isVisible = true
//                        binding.swipeRefreshLayout.isRefreshing = true
//                        binding.included?.layoutError?.isVisible = true;
//                        binding.included.labelError.text = getString(R.string.no_items)
//                        binding.recycler.isVisible = false;
                    } else {
//                        binding.included.layoutError.isVisible = false
//                        binding.recycler.isVisible = true
                    }
                } else {
//                    Log.i("testRefresh", "notLoading size : ${adapter?.snapshot()?.isEmpty()} ")

                    if ((adapter.itemCount ?: 0) <= 0) {
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
                            binding.included.layoutError.isVisible = true
                            binding.included.labelError.text = getString(R.string.no_items)
                            binding.recycler.isVisible = false
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
                                requireContext(),
                                ContextApp.EMOJI_ANGRY + getString(R.string.error_text_label),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        }

        binding.included.reloadPostsBtn.setOnClickListener {
            adapter.refresh();
        }

//        binding.swipeRefreshLayout.setOnRefreshListener {
////            Log.e("testBack1", "adapter refresh : " + CounterPage.pages.size)
//            binding.swipeRefreshLayout.isRefreshing = false;
//            adapter?.refresh()
//        }
    }

    private var categoryID: String? = ""

    fun loadDatas(type: String, categoryID: String?) {
        typeAPI = type;
        this.categoryID = categoryID;

        Log.e("testBack1", " : ${type} ${categoryID} cityID =  ${prefApp.getCityID()}")

//        adapter = LastedPagingAdapter(prefApp = prefApp, lifecycleOwner = viewLifecycleOwner);
//        adapter?.clickAdapterPaging = this;

        lifecycleScope.launch {
            viewModel.lastMarket(
                categoryID,
                GettingIP(requireContext()).deviceIpAddress, prefApp = prefApp, typeAPI
            )?.collectLatest {
                Log.e("testBack1", "adapter launch : ")
                adapter?.submitData(it)
            }
        }
    }

    fun checkProgress(index: Int) {
        progressCheck[index] = true;
        var boolCheck = true;
        for (bool in progressCheck) {
            if (!bool) {
                boolCheck = false;
                break;
            }
        }
        if (boolCheck) {

            showLoadingPedpo.dismiss();
//            binding.swipeRefreshLayout.isRefreshing = false;
        }
    }

    private fun initPagerPoster() {

        binding.viewPagerPoster.offscreenPageLimit = 3;
        binding.viewPagerPoster.adapter = posterAdapter;
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
        binding.viewPagerPoster.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if (binding.viewPagerPoster.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(binding.viewPagerPoster) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset;
                } else {
                    page.translationX = myOffset;
                }
            } else {
                page.translationY = myOffset;
            }
        }

    }

    private fun loadDataPoster() {

        homeViewModel.poster()?.observe(
            viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<PosterData> {
                override fun onSuccess(dataInput: PosterData) {
                    if (dataInput.isSuccess == true) {
                        posterAdapter.updateAdapter(dataInput.data);

                        binding.viewPagerPoster.setCurrentItem(1, false)

                    } else {

                    }
                }

                override fun onException(exception: Exception) {

                }
            })
        )
    }

    @Inject
    lateinit var iconsAdapter: IconsAdapter

    private fun initPagerIcons() {

//        iconsAdapter = IconsAdapter(requireContext())
        iconsAdapter.clickAdapterCategory = this;
        binding.recyclerCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerCategory.adapter = iconsAdapter;

        val dividerItemDecoration = CustomItemDecoration(binding.recyclerCategory.context)
        binding.recyclerCategory.addItemDecoration(dividerItemDecoration);
    }

    fun loadDataIcons() {
        viewModelCategory.selectListCategory(false)
            ?.observe(
                viewLifecycleOwner,
                CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                    override fun onSuccess(dataInput: CategoryData) {

                        checkProgress(1);
                        Log.i(
                            "categoryMarketHome",
                            "0 ${dataInput.isSuccess} ${dataInput.message} ${dataInput.data?.type}"
                        )

                        iconsAdapter.updateAdapter(
                            dataInput.data?.categoryMarket ?: arrayListOf()
                        )

                    }

                    override fun onException(exception: Exception) {

                    }
                })
            )
    }

    private fun loadDataIconsService() {

        viewModelCategory.getCategoriesService(isAll = false)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                override fun onSuccess(dataInput: CategoryData) {
                    Log.i(
                        "categoryMarketHome",
                        "Service ${dataInput.isSuccess} ${dataInput.message} ${dataInput.data?.type}"
                    )

                    iconsAdapter.updateAdapter(
                        dataInput.data?.categoryService ?: arrayListOf()
                    )
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }


    /*Adapter OnItemClick */
    override fun OnItemClickListenerAdapter(marketID: String) {
        val intent = Intent(context, DetailsActivity::class.java);
        intent.putExtra(ContextApp.MARKET_ID, marketID);
        startActivity(intent);
//        var intent = Intent(context, EditMarketActivity::class.java)
//        intent.putExtra(ContextApp.MARKET_ID, marketID);
//        startActivity(intent)
    }


    fun selectBtnCategory(textview: MaterialTextView?, imageView: ImageView?, view: View?) {
        textview?.tag = "true"
        textview?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        imageView?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        view?.isVisible = true
    }

    fun unSelectBtnCategory(textview: MaterialTextView?, imageView: ImageView?, view: View?) {
        textview?.tag = "false"
        textview?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_category_type))
        imageView?.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.text_category_type
            )
        )
        view?.isVisible = false
    }

    /*OnClick onClick MapSearch */
    fun onClickMapSearch(view: View) {
        startActivity(Intent(context,MapActivity::class.java))
    }
    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {
        animationBtnRent()
    }

    fun animationBtnRent() {
        if (binding.tr.tag != null && binding.tr.tag.equals("true"))
            return;
        Handler(Looper.getMainLooper()).postDelayed({
            loadDatas(ContextApp.RENT, null)
            loadDataIcons();
        }, getString(R.integer.duration_anim).toLong())

        if (binding.tS.tag != null && binding.tS.tag.equals("true")) {
            binding.lineBottomSale.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_left
                )
            )
            binding.lineBottomRent.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_refresh_left
                )
            )

        } else if (binding.tService.tag != null && binding.tService.tag.equals("true")) {
            binding.lineBottomService.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_left
                )
            )
            binding.lineBottomRent.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_refresh_left
                )
            )
        }

        selectBtnCategory(binding.tr, binding.img, binding.lineBottomRent)

        unSelectBtnCategory(binding.tS, binding.imgSale, binding.lineBottomSale)
        unSelectBtnCategory(binding.tService, binding.imgService, binding.lineBottomService)
    }

    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {
        if (binding.tS.tag != null && binding.tS.tag.equals("true"))
            return;

        Handler(Looper.getMainLooper()).postDelayed({
            loadDatas(ContextApp.SALE, categoryID = null)
            loadDataIcons();
        }, getString(R.integer.duration_anim).toLong())


        if (binding.tr.tag == null || binding.tr.tag.equals("true")) {
            binding.lineBottomSale.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_refresh_right
                )
            )
            binding.lineBottomRent.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_right
                )
            )
        } else if (binding.tService.tag != null && binding.tService.tag.equals("true")) {
            binding.lineBottomSale.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_refresh_left
                )
            )
            binding.lineBottomService.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_left
                )
            )
        }


        selectBtnCategory(binding.tS, binding.imgSale, binding.lineBottomSale)

        unSelectBtnCategory(binding.tr, binding.img, binding.lineBottomRent)
        unSelectBtnCategory(binding.tService, binding.imgService, binding.lineBottomService)
    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {
        if (binding.tService.tag != null && binding.tService.tag.equals("true"))
            return;

        Handler(Looper.getMainLooper()).postDelayed({
            loadDatas(ContextApp.SERVICE, categoryID = null)
            loadDataIconsService()
        }, getString(R.integer.duration_anim).toLong())

        if (binding.tr.tag == null || binding.tr.tag.equals("true")) {
            binding.lineBottomService.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_refresh_right
                )
            )
            binding.lineBottomRent.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_right
                )
            )
        } else if (binding.tS.tag != null && binding.tS.tag.equals("true")) {
            binding.lineBottomService.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_refresh_right
                )
            )
            binding.lineBottomSale.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.trans_line_right
                )
            )
        }


        selectBtnCategory(binding.tService, binding.imgService, binding.lineBottomService)

        unSelectBtnCategory(binding.tr, binding.img, binding.lineBottomRent)
        unSelectBtnCategory(binding.tS, binding.imgSale, binding.lineBottomSale)
    }


    /*Adapter Visited OnClick */
    override fun OnItemClickListenerAdapter(categoryTO: CategoryTO) {

        categoryIdLast = categoryTO.categoryMarketID.toString();

        loadDatas(typeAPI, categoryIdLast)

    }

    override fun onRefresh() {
        for (index in progressCheck.indices) {
            progressCheck[index] = true;
        }

        iconsAdapter.notifyItemChanged(0)
        categoryIdLast = null;

        animationBtnRent();
        iconsAdapter.notifyDataSetChanged();

    }

    override fun onClickAdapterPaging(
        paginTO: PaginTO?,
        action: String,
        binding: AdapterPaginNewBinding
    ) {
        when (action) {
            ContextApp.DETAILS -> {

                if (paginTO?.nameSite == ContextApp.Pedpo) {
                    var pedpo = Intent(requireContext(), DetailsActivity::class.java);
                    pedpo.putExtra(ContextApp.MARKET_ID, paginTO.marketID);
                    pedpo.putExtra(ContextApp.TYPE_API, paginTO.isService)

                    startActivity(pedpo);
                } else {
                    var intent = Intent(requireContext(), DetailsNeighborActivity::class.java);
                    intent.putExtra(ContextApp.MARKET_ID, paginTO?.neighborMarketID.toString());
                    startActivity(intent);
                }
            }

            ContextApp.BOOKMARK -> {
                if (paginTO?.nameSite.isNullOrEmpty() || paginTO?.nameSite == ContextApp.Pedpo) {
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


    fun addBookmark(paginTO: PaginTO?, binding: AdapterPaginNewBinding) {

        viewModelDetails.addBookmark(
            marketID = paginTO?.marketID,
            if (paginTO?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    Log.i(
                        "testBookmark", "onSuccess: ${paginTO?.type} ${dataInput.isSuccess}" +
                                " ${dataInput.data?.isBookMarkByUser} ${dataInput.message} ${paginTO?.marketID}"
                    )

                    if (dataInput.isSuccess == true) {
                        MyMutable.mutableBookmark.postValue(MyMutable.BooleanBookmark(bookmark = true))
                        paginTO?.isBookMarkByUser = dataInput.data?.isBookMarkByUser;

                        if (dataInput.data?.isBookMarkByUser!!)
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked);
                        else
                            binding.icBookmark.setImageResource(R.drawable.ic_bookmark);
                    }

                }

                override fun onException(exception: Exception) {

                    //                    Log.e("testbookmark", "onException: " + exception.message)
                }

            })
        )
    }

    fun like(paginTO: PaginTO?, binding: AdapterPaginNewBinding) {

        viewModelDetails.like(
            viewTO = ViewTO(
                paginTO?.marketID ?: "",
                GettingIP(requireContext()).deviceIpAddress ?: "",
                if (paginTO?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<LikeData> {
                override fun onSuccess(dataInput: LikeData) {

                    Log.i(
                        "testLike", "onSuccess: ${paginTO?.type} ${dataInput?.isSuccess} " +
                                "${dataInput?.data?.isLikeByIP} ${dataInput?.message} ${GettingIP(requireContext()).deviceIpAddress}"
                    )

                    paginTO?.isLikeByIp = dataInput.data?.isLikeByIP;


                    if (dataInput.data?.isLikeByIP!!) {
                        binding.icLike.setImageResource(R.drawable.ic_liked)
                    } else {
                        binding.icLike.setImageResource(R.drawable.ic_like)
                    }

                }

                override fun onException(exception: Exception) {
                    Log.e("testView", "onSuccess: ${exception.message}")
                }
            })
        )
    }

    fun onClickArea(view: View) {
        var showAreaDialog = ShowAreaDialog();
        showAreaDialog.onReturnPlace = this;
        showAreaDialog.show(childFragmentManager, "area")
    }

    override fun onReturnPlace(placeTO: PlaceTO) {
        binding.tPlace.text = placeTO.name
        loadDatas(type = typeAPI, categoryIdLast)
    }


}