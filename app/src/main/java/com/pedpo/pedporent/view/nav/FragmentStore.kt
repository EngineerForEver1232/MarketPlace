package com.pedpo.pedporent.view.nav

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.FragmentNavStoreBinding
import com.pedpo.pedporent.listener.*
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.model.store.category.CategoryStoreData
import com.pedpo.pedporent.model.store.category.CategoryStoreTO
import com.pedpo.pedporent.model.store.storeList.StoreListData
import com.pedpo.pedporent.model.store.storeList.StoreListTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.*
import com.pedpo.pedporent.view.authentication.LoginActivity
import com.pedpo.pedporent.view.dialog.*
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.store.detailStore.StoreDetailsActivity
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.CustomItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class FragmentStore : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    ClickIconCategoryStore, OnItemClickStoreList , OnCallJust , ReturnContent{

    private lateinit var binding: FragmentNavStoreBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var categoryID: String? = ""
    @Inject
    lateinit var albumAdapter: AlbumStoreAdapter
    @Inject
    lateinit var utills: Utills
    @Inject
    lateinit var showProgressBar: ShowProgressBar
    @Inject
    lateinit var prefApp: PrefApp
    @Inject
    lateinit var iconCategoryAdapter: IconCategoryStore
    @Inject
    lateinit var storeListAdapter: StoreListAdapter
    @Inject
    lateinit var posterAdapter: PosterAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavStoreBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeListAdapter.setCall(this)

        if (prefApp.getToken().isEmpty()) {
            binding.layoutIncludeLogin.constraintDoLogin.isVisible = true
            binding.layoutStore.isVisible = false
        } else {
            binding.layoutIncludeLogin.constraintDoLogin.isVisible = false
            binding.layoutStore.isVisible = true
            initRecycler()
            getStore(null,"")

            initPagerIcons()
            getCategory()
        }


        binding.layoutIncludeLogin.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        binding.eSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getStore(title = title.toString(), categoryID = categoryID)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


    }



    private fun initPagerIcons() {
        iconCategoryAdapter.clickAdapterCategory = this;
        binding.recyclerCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerCategory.adapter = iconCategoryAdapter;

        val dividerItemDecoration = CustomItemDecoration(binding.recyclerCategory.context)
        binding.recyclerCategory.addItemDecoration(dividerItemDecoration);
    }

    fun getCategory() {
        showProgressBar.show(childFragmentManager)
        profileViewModel.getCategoryStore()?.observe(
            viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<CategoryStoreData> {
                override fun onSuccess(dataInput: CategoryStoreData) {
                    showProgressBar.dismiss()

                    if (dataInput.isSuccess == true) {
                        iconCategoryAdapter.updateAdapter(dataInput.data ?: emptyList())
                    } else {
                        Toast.makeText(
                            context,
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onException(exception: Exception) {
                    Toast.makeText(
                        context,
                        getString(R.string.try_again),
                        Toast.LENGTH_SHORT
                    ).show()
                    showProgressBar.dismiss()
                }

            })
        )
    }

    fun initRecycler() {
        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.recycler.adapter = storeListAdapter;
        storeListAdapter.onItemClick = this;
    }

    fun getStore(categoryID: String?, title: String?) {
        profileViewModel.getStoreList(
            categoryID = categoryID ?: "6f35911f-25e5-4fec-a9b9-4ebcce29f016",
        title = title)?.observe(
            viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<StoreListData> {
                override fun onSuccess(dataInput: StoreListData) {
                    if (dataInput.isSuccess == true) {
                        storeListAdapter.updateAdapter(dataInput.data)
                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                    Log.e("storeList", "onException: ${exception.message}")
                }
            })
        )
    }

    override fun onRefresh() {
        getStore(categoryID = categoryID,"")
        binding.swipeRefreshLayout.isRefreshing = false;
    }

    override fun OnItemClickAdapter(categoryTO: CategoryStoreTO) {
        categoryID = categoryTO.categoryStoreID;
        getStore(categoryID = categoryTO.categoryStoreID,"")
    }

    override fun onItemClick(storeListTO: StoreListTO?) {
        val intent = Intent(context, StoreDetailsActivity::class.java)
        intent.putExtra(ContextApp.STORE_ID, storeListTO?.storeID)
        startActivity(intent)
    }

    override fun onCall(storeListTO: StoreListTO?) {
        val dialogRate = RateDialogFragment().newInstance(storeListTO?.storeID.toString())
        dialogRate.setReturn(this)
        dialogRate.show(childFragmentManager,"Rate")
    }

    override fun returnContent(content: String?) {
        getStore(categoryID = categoryID,"")
    }


}