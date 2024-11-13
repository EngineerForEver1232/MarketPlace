//package com.pedpo.pedporent.view.addMarket
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.MenuItem
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pedpo.pedporent.R
//import com.pedpo.pedporent.databinding.ActivityCategoryBinding
//import com.pedpo.pedporent.model.market.category.CategoryData
//import com.pedpo.pedporent.listener.ClickAdapterCategory
//import com.pedpo.pedporent.model.market.category.CategoryTO
//import com.pedpo.pedporent.model.market.editMarket.EditMarketTO
//import com.pedpo.pedporent.utills.ContextApp
//import com.pedpo.pedporent.utills.CustomObserver
//import com.pedpo.pedporent.utills.MyContextWrapper
//import com.pedpo.pedporent.view.adapter.CategoryAdapter
//import com.pedpo.pedporent.view.dialog.ShowProgressBar
//import com.pedpo.pedporent.viewModel.AdMarketViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class CategoryActivityOld : AppCompatActivity(), ClickAdapterCategory {
//
//    lateinit var binding: ActivityCategoryBinding
//
//    @Inject
//    lateinit var categoryAdapter: CategoryAdapter;
//
//    @Inject
//    lateinit var showProgressBar: ShowProgressBar
//
//    private var market: EditMarketTO? = null;
//
//    private val viewModel: AdMarketViewModel by viewModels()
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCategoryBinding.inflate(layoutInflater)
//        binding.lifecycleOwner = this;
////        binding.listener = this;
//        setContentView(binding.root)
//
//        market = intent.getParcelableExtra<EditMarketTO>(ContextApp.MARKET);
//
//        binding.recyclerCategory?.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        var divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_vertical_category)!!)
//
//        binding.recyclerCategory.addItemDecoration(divider);
//        categoryAdapter?.clickAdapterCategory = this;
//        binding.recyclerCategory?.adapter = categoryAdapter;
//
//        getCategories("", "", true)
//
////        getAllCategory()
//
//    }
//
//    var categoryMarketID: String? = null;
//    var subCategoryMarketID: String? = null;
//    var pages = ArrayList<String>();
//
//    fun getCategories(id: String?, name: String?, adStack: Boolean) {
//
//        showProgressBar.show(supportFragmentManager)
//        viewModel.getCategories(id!!).observe(
//            this,
//            CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
//                override fun onSuccess(dataInput: CategoryData) {
//                    showProgressBar.dismiss()
//
////                    if (dataInput?.isSuccess!!) {
////                        if (dataInput.data?.isEmpty()!!) {
////
////                        } else {
////                            if (adStack)
////                                pages.add(id)
////                            categoryMarketID = id;
////                            categoryAdapter?.updateAdapter(dataInput.data!!, type = dataInput.type)
////                        }
////                        Log.i("categoryTest", "onSuccess1: " + dataInput.message)
////                    } else {
////                        subCategoryMarketID = id;
////                        var intent =
////                            Intent(this@CategoryActivityOld, GeneralSpecificationActivity::class.java);
////
////
////                        intent?.putExtra(ContextApp.CATEGORY_ID, categoryMarketID)
////                        intent?.putExtra(ContextApp.SUB_CATEGORY_ID, subCategoryMarketID)
////                        intent?.putExtra(ContextApp.CATEGORY_NAME, name)
////                        activityResultLauncherCategory.launch(intent)
////                        Log.i("categoryTest", "onSuccess2: " + dataInput.message)
////                    }
//                }
//
//                override fun onException(exception: Exception) {
//                    showProgressBar.dismiss()
//
//                    Log.i("categoryTest", "onSuccess: " + exception.message)
//                }
//            })
//        )
//    }
//
////    fun getAllCategory() {
////        viewModel.categoriesAll()?.observe(
////            this, CustomObserver(
////                object : CustomObserver.ResultListener<String> {
////                    override fun onSuccess(dataInput: String) {
////                        Log.i("categoryTestSql", "onSuccess: " + dataInput)
////
////                        getAllCategoryParentChild()
////                    }
////
////                    override fun onException(exception: Exception) {
////                        Log.e("categoryTestSql", "onException: " + exception.message)
////                    }
////
////                })
////        )
////    }
//
//    fun getAllCategoryParentChild() {
////        viewModel.selectParent()
////            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
////                override fun onSuccess(dataInput: CategoryData) {
////
//////                    categoryAdapter?.updateAdapter(dataInput.data!!, type = dataInput.type)
////
////                }
////
////                override fun onException(exception: Exception) {
////
////                }
////            }))
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) finish()
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun OnItemClickListenerAdapter(categoryTO: CategoryTO, type: String) {
//
//        if (type == ContextApp.CATEGORY)
//            getCategories(categoryTO.categoryMarketID, categoryTO.categoryMarketName, true)
//        else {
//            subCategoryMarketID = categoryTO.categoryMarketID;
//            var intent =
//                Intent(this@CategoryActivityOld, GeneralSpecificationActivity::class.java);
//
//            intent?.putExtra(ContextApp.MARKET, market)
//            intent?.putExtra(ContextApp.CATEGORY_ID, categoryMarketID)
//            intent?.putExtra(ContextApp.SUB_CATEGORY_ID, subCategoryMarketID)
//            intent?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.categoryMarketName)
//            activityResultLauncherCategory.launch(intent)
//        }
//
//    }
//
//    private var activityResultLauncherCategory =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            when (result.resultCode) {
//                Activity.RESULT_OK -> {
//                    Toast.makeText(this, "category", Toast.LENGTH_SHORT).show()
//                }
//                ContextApp.RESULT_Ad -> {
//                    setResult(ContextApp.RESULT_Ad, result.data)
//                    finish()
//                }
//                ContextApp.FINISH -> {
//                    setResult(ContextApp.FINISH)
//                    finish()
//                }
//            }
//        }
//
//    override fun onBackPressed() {
//        if (pages.isNotEmpty())
//            pages.removeLast()
//
//        if (pages.isEmpty())
//            super.onBackPressed()
//        else {
//            getCategories(pages[pages.lastIndex], "", false)
//        }
//    }
//
//    /* Onclick */
//    fun onClickBack(view: View) {
//        onBackPressed()
//    }
//
//    /* Onclick */
//    fun onClickicClose(view: View) {
//        setResult(ContextApp.FINISH)
//        finish()
//    }
//}