package com.pedpo.pedporent.view.nav

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterBookmarkBinding
import com.pedpo.pedporent.databinding.FragmentBookmarkBinding
import com.pedpo.pedporent.listener.ClickAdapterBookmark
import com.pedpo.pedporent.model.bookmark.BookmarkData
import com.pedpo.pedporent.model.bookmark.BookmarkTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyMutable
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.BoomarkAdapter
import com.pedpo.pedporent.view.authentication.LoginActivity
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.DetailsViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.ButtonSwitch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : Fragment(), ClickAdapterBookmark {

    @Inject
    lateinit var boomarkAdapter: BoomarkAdapter;

    private val profileViewModel: ProfileViewModel by viewModels();
    private val viewModelDetails: DetailsViewModel by viewModels();

    @Inject
    lateinit var showProgressBar: ShowProgressBar

    @Inject
    lateinit var prefApp: PrefApp

    private lateinit var binding: FragmentBookmarkBinding;

    private var typeAPI = ContextApp.MARKET


    fun newInstance(name: String): BookmarkFragment {
        val categoryFragment = BookmarkFragment();
        val bundle = Bundle();
        bundle.putString("name", name);
        categoryFragment.arguments = bundle;

        return categoryFragment;
    }

    override fun onStart() {
        super.onStart()
        MyMutable.mutableBookmark.postValue(MyMutable.BooleanBookmark(bookmark = false))
        MyMutable.mutableBookmark.observe(viewLifecycleOwner, Observer {
            if (it.bookmark==true) {
                showBookmarks(typeAPI = typeAPI)
            }
        })
        showBookmarks(typeAPI = typeAPI)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        binding.listener = this;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerMyItems.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerMyItems.adapter = boomarkAdapter;
        boomarkAdapter.clickAdapterBookmark = this;


        binding?.constraint.isVisible = !prefApp?.getToken()?.isNullOrEmpty();
        binding?.layoutIncludeLogin.constraintDoLogin.isVisible = prefApp?.getToken()?.isNullOrEmpty();


        binding?.recyclerMyItems.isVisible = !prefApp?.getToken()?.isNullOrEmpty();


        binding?.layoutIncludeLogin?.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }

        binding?.swipeRefreshLayout.setOnRefreshListener {
            showBookmarks(typeAPI = typeAPI)
        }


    }

    var myBookmarks: ArrayList<BookmarkTO>? = null


    fun showBookmarks(typeAPI: String?) {
//        showProgressBar.show(childFragmentManager)

        profileViewModel.bookmarks()?.observe(
            viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<BookmarkData> {
                override fun onSuccess(dataInput: BookmarkData) {
                    Log.i("testGeneral", "onSuccess: ")

                    binding?.swipeRefreshLayout.isRefreshing = false
//                    showProgressBar.dismiss()
                    if (dataInput?.isSuccess == true) {
                        if (typeAPI == ContextApp.MARKET) {
                            myBookmarks = dataInput.data?.myMarkets as ArrayList<BookmarkTO>?

                            boomarkAdapter.updateAdapter(dataInput.data?.myMarkets)
                            binding?.layoutError.isVisible =
                                dataInput.data?.myMarkets?.isEmpty() == true;


                        } else {

                            boomarkAdapter.updateAdapter(dataInput.data?.myService)
                            binding?.layoutError.isVisible =
                                dataInput.data?.myService?.isEmpty() == true;

                        }
                    } else {
                        binding?.layoutError.isVisible = true;
                    }
                }

                override fun onException(exception: Exception) {
//                    showProgressBar.dismiss()
                    binding?.swipeRefreshLayout.isRefreshing = false

                }

            })
        )
    }

    fun addBookmark(bookmark: BookmarkTO?, binding: AdapterBookmarkBinding?,position: Int?) {

        boomarkAdapter.removeItem(position)

        viewModelDetails?.addBookmark(
            marketID = bookmark?.marketID,
            type = if (bookmark?.isService == true) ContextApp.SERVICE else ContextApp.MARKET
        )?.observe(
            this,
            CustomObserver(object :
                CustomObserver.ResultListener<com.pedpo.pedporent.model.BookmarkData> {
                override fun onSuccess(dataInput: com.pedpo.pedporent.model.BookmarkData) {

                    if (dataInput.isSuccess == true) {
                        MyMutable.mutableBookmark.postValue(MyMutable.BooleanBookmark(home = true))

                        if (dataInput.data?.isBookMarkByUser == true) {
//                            binding?.icBookmark?.setImageResource(R.drawable.ic_bookmarked)
                        } else {

//                            boomarkAdapter.notifyItemRemoved(position?:0)
//                            myBookmarks?.removeAt(position?:0)

//                            binding?.icBookmark?.setImageResource(R.drawable.ic_bookmark)

//                            showBookmarks(typeAPI = typeAPI)
                        }

                    }

                }

                override fun onException(exception: Exception) {

                    Log.e("testbookmark", "onException: " + exception.message)
                }

            })
        )
    }

    override fun onClickAdapterPaging(
        bookmark: BookmarkTO?,
        action: String?,
        binding: AdapterBookmarkBinding?,
        position:Int?
    ) {
        when (action) {
            ContextApp.DETAILS -> {
                var intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtra(ContextApp.MARKET_ID, bookmark?.marketID);
                intent.putExtra(ContextApp.TYPE_API, bookmark?.isService)

                startActivity(intent)
            }

            ContextApp.BOOKMARK -> {

                addBookmark(bookmark, binding = binding,position)
            }

        }
    }

    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
            typeAPI = ContextApp.MARKET;
            showBookmarks(ContextApp.MARKET)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(requireContext()).btnSwiche(
            binding.tr, binding.tS, binding.tService,
            binding.lineBottomRent, binding.lineBottomSale, binding.lineBottomService,
            binding.img, binding.imgSale, binding.imgService, 1
        )
    }

    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {

        typeAPI = ContextApp.MARKET;

        Handler(Looper.getMainLooper()).postDelayed({
            showBookmarks(ContextApp.MARKET)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(requireContext()).btnSwiche(
            binding.tS, binding.tr, binding.tService,
            binding.lineBottomSale, binding.lineBottomRent, binding.lineBottomService,
            binding.imgSale, binding.img, binding.imgService, 2
        )
    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {

        typeAPI = ContextApp.SERVICE;


        Handler(Looper.getMainLooper()).postDelayed({
            showBookmarks(ContextApp.SERVICE)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(requireContext()).btnSwiche(
            binding.tService, binding.tr, binding.tS,
            binding.lineBottomService, binding.lineBottomRent, binding.lineBottomSale,
            binding.imgService, binding.img, binding.imgSale, 3
        )
    }


}