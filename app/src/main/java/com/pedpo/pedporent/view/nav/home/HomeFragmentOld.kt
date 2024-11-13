//package com.pedpo.pedporent.view.nav.home
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.AnimationUtils
//import android.widget.ImageView
//import androidx.core.content.ContextCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.isVisible
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import androidx.viewpager2.widget.ViewPager2
//import com.pedpo.pedporent.listener.ClickAdapterVisited
//import com.pedpo.pedporent.view.adapter.*
//import com.pedpo.pedporent.viewModel.HomeViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//import com.pedpo.pedporent.R
//import com.pedpo.pedporent.api.ServiceAPI
//import com.pedpo.pedporent.databinding.FragmentHomeBinding
//import com.pedpo.pedporent.listener.ClickIconCategory
//import com.pedpo.pedporent.model.market.*
//import com.pedpo.pedporent.model.poster.PosterData
//import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.BaseConstants
//
//import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.activity.VisitedMarketPagingActivity
//import com.pedpo.pedporent.utills.ContextApp
//import com.pedpo.pedporent.utills.CustomObserver
//import com.pedpo.pedporent.utills.permission.PrefApp
//import com.pedpo.pedporent.view.details.DetailsActivity
//import com.pedpo.pedporent.view.dialog.progress.ShowLoadingPedpo
//import com.pedpo.pedporent.view.paging.seeMoreLastMarket.activity.LastMarketActivity
//import com.pedpo.pedporent.view.paging.seeMoreRecommended.activity.SeeMRecommendedMarketActivity
//import com.pedpo.pedporent.view.paging.search.activity.SearchActivity
//import com.pedpo.pedporent.viewModel.AdMarketViewModel
//import com.pedpo.pedporent.widget.CustomItemDecoration
//import com.google.android.material.textview.MaterialTextView
//import javax.inject.Inject
//
//
//@AndroidEntryPoint
//class HomeFragmentOld : Fragment(), ClickAdapterVisited, BaseConstants, ClickIconCategory , SwipeRefreshLayout.OnRefreshListener {
//
//
//    lateinit var binding: FragmentHomeBinding
//
//    @Inject
//    lateinit var posterAdapter: PosterAdapter;
//
//
//    @Inject
//    lateinit var visitedAdapter: VisitedAdapter;
//    @Inject
//    lateinit var lastedAdapterNew: LastedAdapterNew;
//
//    var iconsAdapter: IconsAdapter? = null;
//
//    @Inject
//    lateinit var lastedAdapter: LastedAdapter;
//
//    @Inject
//    lateinit var recomendedAdapter: RecomendedAdapter;
//
//    @Inject
//    lateinit var showLoadingPedpo: ShowLoadingPedpo;
//
//    @Inject
//    lateinit var serviceAPI:ServiceAPI;
//    @Inject
//    lateinit var prefApp: PrefApp
//
//    private val homeViewModel: HomeViewModel by viewModels()
//    private val viewModelCategory: AdMarketViewModel by viewModels()
//
//    private var typeAPI = ContextApp.RENT;
//    private var typeCategory :String ?=null;
//
//    private val progressCheck = arrayOf(false, false, false, false)
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        visitedAdapter?.clickAdapter = this;
//        lastedAdapter?.clickAdapter = this;
//        recomendedAdapter?.clickAdapter = this;
//
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
////        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
//        binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()),container,false)
//        binding.lifecycleOwner = this;
//        binding.listener = this;
////        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root;
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        showLoadingPedpo.show(fragmentManager = childFragmentManager);
//
//        initPagerPoster();
//        loadDataPoster();
//
//        initPagerVisited();
//
//        initPagerIcons();
//        loadDataIcons();
//
//        initPagerLasted();
//
//        initPagerRecomended();
//
//        loadDatas(ContextApp.RENT,null)
//
//
//        binding.linearSearch.setOnClickListener {
//            startActivity(Intent(requireContext(), SearchActivity::class.java))
//        }
//
//        binding.swipeRefreshLayout.setOnRefreshListener(this);
//
//    }
//
//    fun loadDatas(type:String,categoryID: String?){
//        typeAPI = type;
//        loadDataVisited(type,categoryID = categoryID);
//        loadDataLasted(type,categoryID = categoryID);
//        loadDataRecomended(type,categoryID = categoryID);
//
//    }
//
//    fun checkProgress(index: Int) {
//        progressCheck[index] = true;
//        var boolCheck = true;
//        for (bool in progressCheck) {
//            if (!bool) {
//                boolCheck = false;
//                break;
//            }
//        }
//        if (boolCheck) {
//            binding.container.visibility = View.VISIBLE;
//            showLoadingPedpo.dismiss();
//            binding.swipeRefreshLayout.isRefreshing = false;
//        }
//    }
//
//    private fun initPagerRecomended() {
//
//        binding.recyclerRecomended.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
//        binding.recyclerRecomended.adapter = recomendedAdapter;
//
//        val dividerItemDecoration = CustomItemDecoration(binding.recyclerRecomended.context)
//        binding.recyclerRecomended.addItemDecoration(dividerItemDecoration);
//
//    }
//
//    private fun loadDataRecomended(type: String,categoryID:String?) {
////        homeViewModel.getMarket()
////            .observe(viewLifecycleOwner, Observer {
////                adapterRecomended?.updateAdapter(it);
////            })
//        homeViewModel.recommendedMarkets(
//            ShowMarketTO(
//                prefApp.getCityID(),
//                categoryID = categoryID,
//                type = type
//            )
//        )?.observe(
//            viewLifecycleOwner,
//            CustomObserver(object : CustomObserver.ResultListener<VisitMarketData> {
//                override fun onSuccess(dataInput: VisitMarketData) {
//                    checkProgress(0);
//                    if (dataInput?.isSuccess!!) {
//                        recomendedAdapter?.updateAdapter(dataInput?.data!!);
//                    } else {
//
//                    }
//                }
//
//                override fun onException(exception: Exception) {
//                    Log.e("showMarkets", "onSuccess: " + exception.message)
//                }
//
//            })
//        )
//    }
//
//    private fun initPagerLasted() {
//        binding.recyclerLasted.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
////        binding.recyclerLasted.adapter = lastedAdapter;
//        binding.recyclerLasted.adapter = lastedAdapterNew;
//        lastedAdapterNew.clickAdapter = this;
//
//        val dividerItemDecoration = CustomItemDecoration(binding.recyclerViewVisited.context)
//        binding.recyclerLasted.addItemDecoration(dividerItemDecoration);
//    }
//
//    private fun loadDataLasted(type:String,categoryID: String?) {
//
//        homeViewModel.lastMarkets(
//            ShowMarketTO(
//                prefApp.getCityID(),
//                categoryID,
//                type = type
//            )
//        ).observe(
//            viewLifecycleOwner,
//            CustomObserver(object : CustomObserver.ResultListener<VisitMarketData> {
//                override fun onSuccess(dataInput: VisitMarketData) {
//                    checkProgress(1);
//                    if (dataInput?.isSuccess!!) {
////                        lastedAdapter?.updateAdapter(dataInput?.data!!);
//                        lastedAdapterNew?.updateAdapter(dataInput?.data!!);
//
//                    } else {
//
//                    }
//                }
//
//                override fun onException(exception: Exception) {
//                    Log.e("showMarkets", "onSuccess: " + exception.message)
//                }
//
//            })
//        )
////        homeViewModel.getReCommended()
////            .observe(viewLifecycleOwner, Observer {
////                adapterLasted?.updateAdapter(it);
////            })
//    }
//
//    private fun initPagerIcons() {
//
//        iconsAdapter = IconsAdapter(requireContext())
//        iconsAdapter?.clickAdapterCategory = this;
//        binding.recyclerCategory.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
//        binding.recyclerCategory.adapter = iconsAdapter;
//
//        val dividerItemDecoration = CustomItemDecoration(binding.recyclerCategory.context)
//        binding.recyclerCategory.addItemDecoration(dividerItemDecoration);
//    }
//
//    private fun loadDataIcons() {
//
//        viewModelCategory.getCategories("")
//            .observe(
//                viewLifecycleOwner,
//                CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
//                    override fun onSuccess(dataInput: CategoryData) {
//                        checkProgress(2);
//                        if (dataInput?.isSuccess!!) {
//                            iconsAdapter?.updateAdapter(dataInput.data!!)
//                        } else {
//
//                        }
//                    }
//
//                    override fun onException(exception: Exception) {
//
//                    }
//
//                })
//            )
//    }
//
//    private fun initPagerVisited() {
//        binding.recyclerViewVisited.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
//        binding.recyclerViewVisited.adapter = visitedAdapter;
//
//        val dividerItemDecoration = CustomItemDecoration(binding.recyclerViewVisited.context)
//        binding.recyclerViewVisited.addItemDecoration(dividerItemDecoration);
//    }
//
//    private fun loadDataVisited(type:String,categoryID: String?) {
//
////        homeViewModel.getvisited()
////            .observe(viewLifecycleOwner, Observer {
////                adapterVisited?.updateAdapter(it)
////            })
//
//        homeViewModel.mostViewMarkets(
//            ShowMarketTO(
//                prefApp.getCityID(),
//                categoryID = categoryID,
//                type = type
//            )
//        )?.observe(
//            viewLifecycleOwner,
//            CustomObserver(object : CustomObserver.ResultListener<VisitMarketData> {
//                override fun onSuccess(dataInput: VisitMarketData) {
//                    checkProgress(3);
//                    if (dataInput?.isSuccess!!) {
//                        visitedAdapter?.updateAdapter(dataInput?.data!!);
//                    } else {
//
//                    }
//                }
//
//                override fun onException(exception: Exception) {
//                    Log.e("showMarkets", "onSuccess: " + exception.message)
//                }
//
//            })
//        )
//
//    }
//
//    private fun initPagerPoster() {
//
//        binding.viewPagerPoster?.offscreenPageLimit = 3;
//        binding.viewPagerPoster?.adapter = posterAdapter;
//        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
//        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
//        binding.viewPagerPoster.setPageTransformer { page, position ->
//            val myOffset = position * -(2 * pageOffset + pageMargin)
//            if (binding.viewPagerPoster.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
//                if (ViewCompat.getLayoutDirection(binding.viewPagerPoster) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                    page.translationX = -myOffset;
//                } else {
//                    page.translationX = myOffset;
//                }
//            } else {
//                page.translationY = myOffset;
//            }
//        }
//
//    }
//
//    private fun loadDataPoster() {
////        homeViewModel.getPoster().observe(viewLifecycleOwner, Observer {
////            adapterPoster?.updateAdapter(it);
////            binding.viewPagerPoster.setCurrentItem(1, false)
////        })
//
//        homeViewModel.poster()?.observe(viewLifecycleOwner,
//            CustomObserver(object : CustomObserver.ResultListener<PosterData> {
//                override fun onSuccess(dataInput: PosterData) {
//                    if (dataInput.isSuccess == true) {
//                        posterAdapter?.updateAdapter(dataInput?.data);
//                        binding.viewPagerPoster.setCurrentItem(1, false)
//
//                    } else {
//
//                    }
//                }
//
//                override fun onException(exception: Exception) {
//
//                }
//            })
//        )
//    }
//
//    /*Adapter OnItemClick */
//    override fun OnItemClickListenerAdapter(marketID: String) {
//        var intent = Intent(context, DetailsActivity::class.java)
//        intent.putExtra(ContextApp.MARKET_ID, marketID);
//        startActivity(intent)
////        var intent = Intent(context, EditMarketActivity::class.java)
////        intent.putExtra(ContextApp.MARKET_ID, marketID);
////        startActivity(intent)
//    }
//
//
//    fun selectBtnCategory(textview:MaterialTextView?,imageView:ImageView?,view:View?){
//        textview?.tag = "true"
//        textview?.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
//        imageView?.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
//        view?.isVisible = true
//    }
//    fun unSelectBtnCategory(textview:MaterialTextView?,imageView:ImageView?,view:View?){
//        textview?.tag = "false"
//        textview?.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_category_type))
//        imageView?.setColorFilter(ContextCompat.getColor(requireContext(),R.color.text_category_type))
//        view?.isVisible = false
//    }
//
//    /*OnClick onClick Button Category Rent */
//    fun onClickBtnCategoryRent(view: View) {
//        animationBtnRent()
//    }
//    fun animationBtnRent(){
//        if (binding.tr.tag !=null && binding.tr.tag.equals("true"))
//            return;
//        loadDatas(ContextApp.RENT, typeCategory)
//
//        if (binding.tS.tag!=null && binding.tS.tag.equals("true")){
//            binding.lineBottomSale.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_left))
//            binding.lineBottomRent.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_refresh_left))
//
//        }
//        else if (binding.tService.tag!=null && binding.tService.tag.equals("true")){
//            binding.lineBottomService.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_left))
//            binding.lineBottomRent.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_refresh_left))
//        }
//
//        selectBtnCategory(binding.tr,binding.img,binding.lineBottomRent)
//
//        unSelectBtnCategory(binding.tS,binding.imgSale,binding.lineBottomSale)
//        unSelectBtnCategory(binding.tService,binding.imgService,binding.lineBottomService)
//    }
//
//    /*OnClick onClick Button Category Sale */
//    fun onClickBtnCategorySale(view: View) {
//        if (binding.tS.tag !=null && binding.tS.tag.equals("true"))
//            return;
//        loadDatas("Sale",typeCategory)
//
//        if (binding.tr.tag ==null || binding.tr.tag.equals("true")){
//            binding.lineBottomSale.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_refresh_right))
//            binding.lineBottomRent.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_right))
//        }
//        else if(binding.tService.tag !=null && binding.tService.tag.equals("true")){
//            binding.lineBottomSale.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_refresh_left))
//            binding.lineBottomService.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_left))
//        }
//
//
//        selectBtnCategory(binding.tS,binding.imgSale,binding.lineBottomSale)
//
//        unSelectBtnCategory(binding.tr,binding.img,binding.lineBottomRent)
//        unSelectBtnCategory(binding.tService,binding.imgService,binding.lineBottomService)
//    }
//    /*OnClick onClick Button Category Service */
//    fun onClickBtnCategoryService(view: View) {
//
//        if (binding.tService.tag !=null && binding.tService.tag.equals("true"))
//            return;
//
//        if (binding.tr.tag ==null || binding.tr.tag.equals("true")){
//            binding.lineBottomService.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_refresh_right))
//            binding.lineBottomRent.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_right))
//        }
//        else if(binding.tS.tag !=null && binding.tS.tag.equals("true")){
//            binding.lineBottomService.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_refresh_right))
//            binding.lineBottomSale.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_line_right))
//        }
//
//        selectBtnCategory(binding.tService,binding.imgService,binding.lineBottomService)
//
//        unSelectBtnCategory(binding.tr,binding.img,binding.lineBottomRent)
//        unSelectBtnCategory(binding.tS,binding.imgSale,binding.lineBottomSale)
//    }
//
//    /*OnClick See More Visited */
//    fun seeMoreVisited(view: View) {
//        startActivity(Intent(requireContext(), VisitedMarketPagingActivity::class.java))
//    }
//
//    /*OnClick See More Recommended */
//    fun seeMoreReComended(view: View) {
//        startActivity(Intent(requireContext(), SeeMRecommendedMarketActivity::class.java))
//    }
//    /*OnClick See More Recommended */
//    fun seeMoreLasted(view: View) {
//
//        var last = Intent(requireContext(), LastMarketActivity::class.java);
//        last.putExtra(ContextApp.TYPE_API,typeAPI)
//        startActivity(last)
//    }
//
//    /*Adapter Visited OnClick */
//    override fun OnItemClickListenerAdapter(categoryTO: CategoryTO) {
//
//        typeCategory = categoryTO.categoryMarketID.toString();
//
//        loadDataVisited(type = typeAPI,categoryID = categoryTO.categoryMarketID);
//        loadDataRecomended(type = typeAPI,categoryID = categoryTO.categoryMarketID);
//        loadDataLasted(type = typeAPI,categoryID = categoryTO.categoryMarketID);
//
//    }
//
//    override fun onRefresh() {
//        for (index in progressCheck.indices) {
//            progressCheck[index] = true;
//        }
//
//        iconsAdapter?.notifyItemChanged(0)
//        typeCategory = null;
//
//        animationBtnRent();
////            loadDataPoster();
//        loadDataRecomended(ContextApp.RENT,null);
//        loadDataVisited(ContextApp.RENT,null);
//        iconsAdapter?.notifyDataSetChanged();
//        loadDataLasted(ContextApp.RENT,null);
//
//    }
//
//}