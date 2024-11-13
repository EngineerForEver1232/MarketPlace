package com.pedpo.pedporent.view.nav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.FragmentCategoryNavBinding
import com.pedpo.pedporent.listener.ClickAdapterCategory
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.market.category.CategoryTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.CategoryAdapter
import com.pedpo.pedporent.view.addMarket.GeneralSpecificationActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.paging.marketWithCategory.viewModel.CategoryMarketViewModel
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.CustomLoadStateAdapter
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.LastedPagingAdapter
import com.pedpo.pedporent.viewModel.AdMarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import androidx.core.view.isVisible


@AndroidEntryPoint
class CategoryFragment : Fragment(), ClickAdapterCategory {


    lateinit var binding: FragmentCategoryNavBinding

    @Inject
    lateinit var categoryAdapter: CategoryAdapter;
    @Inject
    lateinit var prefApp: PrefApp;

    @Inject
    lateinit var showProgressBar: ShowProgressBar

    private var adapter: LastedPagingAdapter? = null;

    private val viewModelLastMarket: CategoryMarketViewModel by viewModels();

    private val viewModel: AdMarketViewModel by viewModels()
    var parentCategoryID: String? = null;
    var childCategoryID: String? = null;
//    var pages = ArrayList<String>();


    fun newInstance(name: String): CategoryFragment {
        var categoryFragment = CategoryFragment();
        var bundle = Bundle();
        bundle.putString("name", name)
        categoryFragment?.arguments = bundle;

        return categoryFragment;
    }

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (CounterPage.pages.isEmpty()) {


                    Log.i("testBack", "stack size : " + CounterPage.pages.size)
                } else if (CounterPage.pages.get(CounterPage.pages.lastIndex) == "paging") {
                    Log.i("testBack", "paging : " + CounterPage.pages.size)
                    binding.recyclerCategory.visibility = View.VISIBLE;
                    binding.swipeRefreshLayout.visibility = View.GONE;
                } else {
                    Log.i("testBack", "server : " + CounterPage.pages.size)
                    getParentCategory(false)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCategoryNavBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this;
//        binding.listener = this;
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerCategory?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.divider_vertical_category)!!)

        binding.recyclerCategory.addItemDecoration(divider)
        categoryAdapter?.clickAdapterCategory = this;
        binding.recyclerCategory?.adapter = categoryAdapter;


//        getAllCategory_ForInsert()
        getParentCategory(true)
        init()
    }

    fun init() {
        binding.recyclerMarket.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        adapter = LastedPagingAdapter(prefApp = prefApp,viewLifecycleOwner);

        binding.recyclerMarket.adapter = adapter

        viewModelLastMarket.initCotent(null, GettingIP(requireContext()).deviceIpAddress,prefApp.getCityID())
        lifecycleScope.launch {
            viewModelLastMarket.posts.collectLatest {

                Log.e("testBack1", "adapter: " + CounterPage.pages.size)
                adapter?.submitData(it)
            }
        }

        binding.recyclerMarket.adapter = adapter?.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter?.retry()
            }
        )

        lifecycleScope.launch {
            adapter?.loadStateFlow?.collect { loadState ->

                val refreshState = loadState.refresh;

                binding.recyclerMarket.isVisible = refreshState is LoadState.NotLoading
                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading
                binding.layoutError.isVisible = refreshState is LoadState.Error

                if (refreshState is LoadState.Error) {
                    when (refreshState.error as Exception) {
                        is HttpException ->
                            binding.labelError.text = getString(R.string.internal_error)
                        is IOException ->
                            binding.labelError.text = getString(R.string.label_no_internet)
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

        binding.reloadPostsBtn.setOnClickListener {
            adapter?.refresh();
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false;
            adapter?.refresh();
        }

    }

    fun paging(idCategory: String) {
        viewModelLastMarket.initCotent(idCategory, GettingIP(requireContext()).deviceIpAddress,prefApp.getCityID())
        binding.swipeRefreshLayout.isRefreshing = false;
        adapter?.refresh();

        binding.recyclerCategory.visibility = View.GONE;
        binding.swipeRefreshLayout.visibility = View.VISIBLE;
        if (CounterPage.pages[CounterPage.pages.lastIndex] != "paging")
            CounterPage.pages.add("paging");

    }


//    fun getAllCategory_ForInsert() {
//        viewModel.categoriesAll()?.observe(
//            viewLifecycleOwner, CustomObserver(
//                object : CustomObserver.ResultListener<String> {
//                    override fun onSuccess(dataInput: String) {
//
//                        getParentCategory(true)
//                    }
//
//                    override fun onException(exception: Exception) {
//                        Log.e("categoryTestSql", "onException: " + exception.message)
//                    }
//
//                })
//        )
//    }

    fun getParentCategory(adStack: Boolean) {
//        viewModel.selectParent()
//            ?.observe(viewLifecycleOwner,
//                CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
//                    override fun onSuccess(dataInput: CategoryData) {
//                        if (dataInput.data != null && dataInput.data?.isEmpty()!!) {
//                            getAllCategory_ForInsert()
//                        } else {
//                            categoryAdapter?.updateAdapter(dataInput.data!!, type = dataInput.type)
////                            if (adStack)
////                                CounterPage.pages.add("")
//                            parentCategoryID = "";
//                        }
//                    }
//
//                    override fun onException(exception: Exception) {
//
//                    }
//                }))
    }

    fun getChildCategory(parentID: String?, name: String?, adStack: Boolean) {
        viewModel.selectChild(parentID = parentID)
            ?.observe(viewLifecycleOwner,
                CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                    override fun onSuccess(dataInput: CategoryData) {
//
//                        if (dataInput.data != null && dataInput.data?.isEmpty()!!) {
//                            getAllCategory_ForInsert()
//                        } else {
//                            categoryAdapter?.updateAdapter(dataInput.data!!, type = dataInput.type)
//                            if (adStack)
//                                CounterPage.pages.add(parentID!!)
//                            parentCategoryID = parentID;
//                        }
                    }

                    override fun onException(exception: Exception) {

                    }
                }))
    }

    @Inject
    lateinit var prefLang: PrefManagerLanguage;

    override fun OnItemClickListenerAdapter(categoryTO: CategoryTO, type: String) {

        if (type == ContextApp.CATEGORY) {
            getChildCategory(categoryTO.categoryMarketID, categoryTO.categoryMarketName, true)
        } else {
            childCategoryID = categoryTO.categoryMarketID;

            binding.tTitle.text = if (prefLang.language == ContextApp.EN)
                categoryTO.englishCategoryMarketName;
            else
                categoryTO.categoryMarketName;

            paging(childCategoryID!!)
            var intent =
                Intent(requireContext(), GeneralSpecificationActivity::class.java);

//            intent?.putExtra(ContextApp.MARKET, market)
            intent?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
            intent?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)
            intent?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.categoryMarketName)
//            activityResultLauncherCategory.launch(intent)

//            binding.swipeRefreshLayout.visibility = View.VISIBLE;
//            binding.recyclerCategory.visibility = View.GONE;
        }
    }


}